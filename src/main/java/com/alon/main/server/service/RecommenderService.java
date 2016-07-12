package com.alon.main.server.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public final class RecommenderService {

    private final static Logger logger = Logger.getLogger(RecommenderService.class);

    @Autowired
    RatingService ratingService;

    private JavaSparkContext sc;
    private JavaRDD<Rating> fileRatings;
    private ModelContainer modelContainer = new ModelContainer();
    private List<Integer> defaultMovieList;

    @PostConstruct
    private void init() {
        SparkConf sparkConf = new SparkConf().setAppName("JavaALS");
        sparkConf.setMaster("local[8]").set("spark.executor.memory", "4g");
        sc = new JavaSparkContext(sparkConf);
        setModel();
        loadRatingFromFile();
        setDefaultMovieList();
    }

    private void setDefaultMovieList() {
        JavaRDD<Rating> ratings = fileRatings.filter(rating -> rating.rating() > 4);

        JavaPairRDD<Rating, Integer> pairs = JavaPairRDD.fromJavaRDD(ratings.map(rating -> new Tuple2<>(rating, 1)));
        JavaPairRDD<Rating, Integer> ratingToSum = pairs.reduceByKey((a, b) -> a + b);

        JavaPairRDD<Integer, Rating> sumToRating = JavaPairRDD.fromJavaRDD(ratingToSum.map(Tuple2::swap));
        defaultMovieList = sumToRating.sortByKey().values().map(Rating::product).collect();
    }

    @Deprecated  // need to delete this - only use for in memory movieBaseDao.
    public JavaSparkContext getJavaSparkContext(){
        return sc;
    }

    @PreDestroy
    private void destroy() {
        sc.stop();
    }

    public List<Integer> recommend(Integer user, Integer recommendationNumber) {

        logger.debug("Ask for recommendation for " + "userId : " + user + ", user: " + user + ", recommandNum: " + recommendationNumber);

        MatrixFactorizationModel model = modelContainer.getReadModel();

        List<Rating> ratingsList;
        try{
            Rating[] ratings = model.recommendProducts(user, recommendationNumber);
            ratingsList = Arrays.asList(ratings);

        }catch (Exception e){
            logger.error("Error to recommend for user: " + user, e);
            return defaultMovieList.subList(0, recommendationNumber);
        }finally {
            modelContainer.returnReadModel();
        }

        return ratingsList.stream().map(Rating::product).collect(Collectors.toList());
    }

    private void setModel() {
        Configuration conf = new Configuration();

        try {
            Path path = new Path(MODEL_PATH);
            long dateLong = FileSystem.get(conf).getFileStatus(path).getModificationTime();
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.systemDefault());
            LocalDateTime now = LocalDateTime.now();
            if (date.plusDays(2).isBefore(now)) {
                FileSystem.get(conf).delete(path, true);
            }
            modelContainer.loadModel();

        } catch (Exception e) {
            MatrixFactorizationModel tempModel = createModel(true);
            modelContainer.setModel(tempModel);
        }
    }

    @Async
    public void setModelAsync() {

        Runnable runnable = () -> {
            try {
                Configuration conf = new Configuration();
                Path path = new Path(MODEL_TEMP_PATH);
                try {
                    FileSystem.get(conf).delete(path, true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                MatrixFactorizationModel model = createModel(false);
                modelContainer.setModel(model);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

    private MatrixFactorizationModel createModel(Boolean loadFromFiles) {

        int rank = 10;
        int iterations = 10;

        JavaRDD<Rating> ratings = loadRatings(loadFromFiles);

        return ALS.trainImplicit(ratings.rdd(), rank, iterations);
    }

    private JavaRDD<Rating> loadRatings(Boolean loadFromFiles) {
        if (loadFromFiles){
            loadRatingFromFile();
        }

        List<Rating> dbRatingList = ratingService.getAllToList().stream().map(this::toRating).collect(Collectors.toList());

        JavaRDD<Rating> dbRatings = sc.parallelize(dbRatingList);
        return fileRatings.union(dbRatings).distinct();
    }

    private void loadRatingFromFile() {
        if (fileRatings == null){
            JavaRDD<String> lines = sc.textFile(RATINGS_PATH);
            String first = lines.first();
            fileRatings = lines.filter(line -> !line.equals(first)).map(new ParseRating());
        }
    }

    private Rating toRating(com.alon.main.server.entities.Rating rating) {
        return new Rating(rating.getUserId(), rating.getMovieId(), rating.getRating());
    }


    private double testModel(JavaRDD<Rating> ratings, MatrixFactorizationModel model) {

        JavaRDD<Tuple2<Object, Object>> userProducts = ratings.map(r -> new Tuple2<Object, Object>(r.user(), r.product()));

        // Evaluate the model on rating data
        JavaRDD<Tuple2<Tuple2<Integer, Integer>, Double>> predictionsNorPair = model
                .predict(JavaRDD.toRDD(userProducts))
                .toJavaRDD()
                .map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating()));

        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(predictionsNorPair);

        JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD
                .fromJavaRDD(ratings.map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())))
                .join(predictions)
                .values();

        return ratesAndPreds.mapToDouble(pair -> (pair._1() - pair._2()) * (pair._1() - pair._2())).mean();
    }

    private static class ParseRating implements Function<String, Rating> {

        @Override
        public Rating call(String line) {
            String[] tok = COMMA.split(line);
            int userId = Integer.parseInt(tok[0]);
            int movieId = Integer.parseInt(tok[1]);
            double rating = Double.parseDouble(tok[2]);
            return new Rating(userId, movieId, rating);
        }
    }

    private static void deleteAndAddAll(MatrixFactorizationModel model) {
        Configuration conf = new Configuration();

        deleteAndAddDir(model.userFeatures().toJavaRDD(), conf, USER_FEATURES_PATH);
        deleteAndAddDir(model.productFeatures().toJavaRDD(), conf, PRODUCT_FEATURES_PATH);
    }

    private static void deleteAndAddDir(JavaRDD<Tuple2<Object, double[]>> model, Configuration conf, String pathString) {
        Path path = new Path(pathString);
        try {

            FileSystem.get(conf).delete(path, true);
            model.map(RecommenderService::FeaturesToString).saveAsTextFile(pathString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String FeaturesToString(Tuple2<Object, double[]> feature) {
        return feature._1() + "," + Arrays.toString(feature._2());
    }

    private class ModelContainer {
        private MatrixFactorizationModel model;

        ReadWriteLock lock = new ReentrantReadWriteLock();

        void loadModel(){
            WriteLockModel();
            this.model = MatrixFactorizationModel.load(sc.sc(), MODEL_PATH);
            returnWriteModel();
        }

        void setModel(MatrixFactorizationModel model){


            WriteLockModel();

            Configuration conf = new Configuration();
            Path path = new Path(MODEL_PATH);
            try {
                FileSystem.get(conf).delete(path, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            this.model = model;
            model.save(sc.sc(), MODEL_PATH);
            deleteAndAddAll(model);

            returnWriteModel();
        }

        MatrixFactorizationModel getReadModel(){
            lock.readLock().lock();
            return model;
        }

        void WriteLockModel(){
            lock.writeLock().lock();
        }

        void returnReadModel(){
            lock.readLock().unlock();
        }

        void returnWriteModel(){
            lock.writeLock().unlock();
        }
    }


}