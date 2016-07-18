package com.alon.main.server.service;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.RDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Autowired
    MovieService movieService;

    @Value("${spark.thread.num}")
    private String threadNumber;

    @Value("${spark.memory.executor.size}")
    private String executorMemorySize;

    @Value("${spark.memory.driver.size}")
    private String driverMemorySize;

    @Value("${spark.test}")
    private Boolean testMode;

    @Value("${recommender.path.model}")
    private String MODEL_PATH;

    @Value("${recommender.path.model.temp}")
    private String MODEL_TEMP_PATH;

    @Value("${recommender.path.data.ratings}")
    private String RATINGS_PATH;

    @Value("${recommender.path.user.features}")
    private String USER_FEATURES_PATH;

    @Value("${recommender.path.product.features}")
    private String PRODUCT_FEATURES_PATH;

    private JavaSparkContext sc;
    private JavaRDD<Rating> fileRatings;
    private ModelContainer modelContainer = new ModelContainer();
    private List<Integer> defaultMovieList;
    private Map<Integer, Integer> userToNumberMap;

    private static Integer likePrefix = 99900000;
    private static Integer unLikePrefix = 66600000;

    @PostConstruct
    private void init() {
        SparkConf sparkConf = new SparkConf().setAppName("JavaALS");
        String master = "local[" + threadNumber + "]";
        String executorMemory = executorMemorySize +"g";
        String driverMemory = driverMemorySize +"g";

        logger.debug("#################################");
        logger.debug("###   RecommenderService      ###");
        logger.debug("###   " + master + "                ###");
        logger.debug("### spark.executor.memory: " + executorMemorySize + "  ###");
        logger.debug("### spark.driver.memory: " + driverMemorySize + "    ###");
        logger.debug("#################################");

        logger.debug("sparkConf: " + sparkConf.toDebugString());
        sparkConf.
                setMaster(master).
                set("spark.executor.memory", executorMemory);
//                set("spark.driver.memory", driverMemory);

        if (StringUtils.isNumeric(driverMemorySize)){
            sparkConf.set("spark.driver.memory", driverMemory);
        }

        if (testMode){
            sparkConf.set("spark.testing", "true");
        }

        //
        int mb = 1024*1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();


        logger.debug("##### Heap utilization statistics [MB] #####");

        //Print used memory
        logger.debug("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / mb);

        //Print free memory
        logger.debug("Free Memory:"
                + runtime.freeMemory() / mb);

        //Print total available memory
        logger.debug("Total Memory:" + runtime.totalMemory() / mb);

        //Print Maximum available memory
        logger.debug("Max Memory:" + runtime.maxMemory() / mb);
        //

        sc = new JavaSparkContext(sparkConf);
        setModel();
        loadRatingFromFile();
        setDefaultMovieList();
//        setUserToNumberMap();
//        updateUser(10);
    }

    private void setDefaultMovieList() {

        if (testMode){
            defaultMovieList = Arrays.asList(63082, 110, 1960, 1043, 2858, 47, 31410, 527, 6539, 4886, 786, 25993, 36, 954, 2686, 3703, 1590, 1163, 39, 50, 61026);
        }else {

            JavaRDD<Rating> ratings = fileRatings.filter(rating -> rating.rating() > 4);

            JavaPairRDD<Rating, Integer> pairs = JavaPairRDD.fromJavaRDD(ratings.map(rating -> new Tuple2<>(rating, 1)));
            JavaPairRDD<Rating, Integer> ratingToSum = pairs.reduceByKey((a, b) -> a + b);

            JavaPairRDD<Integer, Rating> sumToRating = JavaPairRDD.fromJavaRDD(ratingToSum.map(Tuple2::swap));
            defaultMovieList = sumToRating.sortByKey().values().map(Rating::product).collect();
        }
    }
//
//    private void setUserToNumberMap() {
//
//        JavaRDD<Rating> ratings = loadRatings(true);
//
//        JavaPairRDD<Integer, Integer> pairs = JavaPairRDD.fromJavaRDD(ratings.map(rating -> new Tuple2<>(rating.user(), 1)));
//        JavaPairRDD<Integer, Integer> userToSum = pairs.reduceByKey((a, b) -> a + b);
//
//        userToNumberMap = userToSum.collectAsMap();
//
//    }

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

    public  void updateModel(Rating newRating) {

        logger.debug("Update model for rating: " + newRating);

        int product = newRating.product();
        int user = newRating.user();

        MatrixFactorizationModel model = modelContainer.getReadModel();
        RDD<Tuple2<Object, double[]>> scalaUserFeatures = model.userFeatures();
        modelContainer.returnReadModel();

        JavaRDD<Tuple2<Object, double[]>> userFeatures = scalaUserFeatures.toJavaRDD();

        Integer prefix = newRating.rating() > 3 ? likePrefix : unLikePrefix;

        int ratingUserNum = product + prefix;

        List<double[]> userVector = JavaPairRDD.fromJavaRDD(userFeatures).lookup(user);
        List<double[]> ratingUser = JavaPairRDD.fromJavaRDD(userFeatures).lookup(ratingUserNum);
        List<double[]> newUserVector = addVectors(userVector, ratingUser, null);

        List<Tuple2<Object, double[]>> newUserList = new ArrayList<>();
        newUserList.add(new Tuple2<>(user, newUserVector.get(0)));

        JavaRDD<Tuple2<Object, double[]>> newUserRdd = sc.parallelize(newUserList);

        JavaRDD<Tuple2<Object, double[]>> combinedRdd = userFeatures.filter(x -> !x._1().equals(user)).union(newUserRdd);

        MatrixFactorizationModel newModel = new MatrixFactorizationModel(model.rank(), combinedRdd.rdd(), model.productFeatures());

        modelContainer.setModel(newModel, false);

    }

    private List<double[]> addVectors(List<double[]> userVector6, List<double[]> userVector7, Integer num) {
        double[] userVector67List = new double[userVector6.get(0).length];
        int retio = 1;
        if (num != null){
            retio = 1 / (num + 1);
        }

        for (int i = 0; i < 10; i++) {
            Double pluse = userVector6.get(0)[i] + (userVector7.get(0)[i] * retio);
            userVector67List[i] = pluse;
        }

        List<double[]> userVector67 = new ArrayList<>();
        userVector67.add(userVector67List);

        return userVector67;
    }

    private void setModel() {
        Configuration conf = new Configuration();

        if (testMode){
            modelContainer.loadModel();
        }else {
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
                modelContainer.setModel(tempModel, true);
            }
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
                modelContainer.setModel(model, true);
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

        JavaRDD<Integer> loveIts = fileRatings.groupBy(Rating::product).keys();

        JavaRDD<Rating> loveIt = fileRatings.groupBy(Rating::product).keys().map(new ParseLikeRatingFromProduct());
//        JavaRDD<Rating> hateIt = fileRatings.groupBy(Rating::product).keys().map(new ParseUnLikeRatingFromProduct());

        JavaRDD<Rating> dbRatings = sc.parallelize(dbRatingList);
        return fileRatings.
                union(dbRatings).
                union(loveIt).
//                union(hateIt).
                distinct();
    }

    private void loadRatingFromFile() {
        if (!testMode && fileRatings == null){
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


    private static class ParseUnLikeRatingFromProduct implements Function<Integer, Rating> {

        @Override
        public Rating call(Integer product) {
            return new Rating(unLikePrefix + product, product, 0);
        }
    }

    private static class ParseLikeRatingFromProduct implements Function<Integer, Rating> {

        @Override
        public Rating call(Integer product) {
            return new Rating(likePrefix + product, product, 5);
        }
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

    private void deleteAndAddAll(MatrixFactorizationModel model) {
        Configuration conf = new Configuration();

//        deleteAndAddDir(model.userFeatures().toJavaRDD(), conf, USER_FEATURES_PATH);
//        deleteAndAddDir(model.productFeatures().toJavaRDD(), conf, PRODUCT_FEATURES_PATH);
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

        void setModel(MatrixFactorizationModel model, boolean save){


            WriteLockModel();
            this.model = model;

            if (save){
                Configuration conf = new Configuration();
                Path path = new Path(MODEL_PATH);
                try {
                    FileSystem.get(conf).delete(path, true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                model.save(sc.sc(), MODEL_PATH);
                deleteAndAddAll(model);
            }


            returnWriteModel();
        }

        MatrixFactorizationModel getReadModel(){
            lock.readLock().lock();
            return model;
        }

        void returnReadModel(){
            lock.readLock().unlock();
        }

        private void WriteLockModel(){
            lock.writeLock().lock();
        }

        private void returnWriteModel(){
            lock.writeLock().unlock();
        }
    }


}