package com.alon.main.server.service;

import com.alon.main.server.dao.rating.RatingMorphiaDaoImpl;
import com.google.common.collect.Iterables;
import org.apache.commons.math3.analysis.function.Identity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.RDD;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public final class RecommenderService {

    @Autowired
    RatingService ratingService;


    private JavaSparkContext sc;
    private MatrixFactorizationModel model;

    @PostConstruct
    @Async
    private void init() {
        SparkConf sparkConf = new SparkConf().setAppName("JavaALS");
        sparkConf.setMaster("local[8]").set("spark.executor.memory", "4g");
        sc = new JavaSparkContext(sparkConf);
        setModel();
    }

    @Deprecated  // need to delete this - only use for in memory movieBaseDao.
    public JavaSparkContext getJavaSparkContext(){
        return sc;
    }

    @PreDestroy
    private void destroy() {
        sc.stop();
    }

//    https://github.com/apache/spark/tree/master/examples/src/main/java/org/apache/spark/examples/mllib

    public List<Integer> recommend(Integer user, Integer recommendationNumber) {

        Rating[] ratings = model.recommendProducts(user, recommendationNumber);
        List<Rating> ratingsList = Arrays.asList(ratings);
        return ratingsList.stream().map(Rating::product).collect(Collectors.toList());
    }

    public List<Integer> recommend(Integer recommendationNumber) {
        JavaRDD<Tuple2<Object, Rating[]>> userToRatings = model.recommendProductsForUsers(1).toJavaRDD();

        JavaRDD<Rating> counvterToMovieId = userToRatings.map(Tuple2::_2).map(x -> x[0]);

        long counvtwerToMovieId = userToRatings.map(Tuple2::_2).map(x -> x[0]).count();

        JavaRDD<Tuple2<Integer, Integer>> counterToMovieId = userToRatings.
                map(new UserRatingToMovieIds()).
                flatMap(List::iterator).
                groupBy(x -> x).
                mapValues(Iterables::size).
                map(x -> new Tuple2<>(x._2(), x._1));

        List<Integer> response = JavaPairRDD.fromJavaRDD(counterToMovieId).
                sortByKey(false).
                take(recommendationNumber).
                stream().map(Tuple2::_2).
                collect(Collectors.toList());


        return response ; //ratingsList.stream().map(Rating::product).collect(Collectors.toList());
    }


    private static class UserRatingToMovieIds implements Function<Tuple2<Object, Rating[]>, List<Integer>> {

        @Override
        public List<Integer> call(Tuple2<Object, Rating[]> line) {

            return Arrays.asList(line._2()).
                    stream().
                    map(Rating::product).
                    collect(Collectors.toList());
        }
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
            model = MatrixFactorizationModel.load(sc.sc(), MODEL_PATH);
        } catch (Exception e) {
            model = createModel();
            try {
                saveModel(model);
            } catch (FileAlreadyExistsException f) {
                f.printStackTrace();
            }
        }
    }

    @Async
    public void setModelAsync() {
        Configuration conf = new Configuration();
        Path path = new Path(MODEL_PATH);
        try {
            FileSystem.get(conf).delete(path, true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        model = createModel();
        try {
            saveModel(model);
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    private MatrixFactorizationModel createModel() {

        int rank = 10;
        int iterations = 10;

        JavaRDD<Rating> ratings = loadRatings();

        return ALS.trainImplicit(ratings.rdd(), rank, iterations);
    }

    private void saveModel(MatrixFactorizationModel modelTOSave) throws FileAlreadyExistsException {

        JavaRDD<Rating> ratings = loadRatings();

        modelTOSave.save(sc.sc(), MODEL_PATH);

        deleteAndAddAll(modelTOSave);
    }



    private JavaRDD<Rating> loadRatings() {
        JavaRDD<String> lines = sc.textFile(RATINGS_PATH);
        String first = lines.first();

        List<Rating> dbRatingList = ratingService.getAllToList().stream().map(this::toRating).collect(Collectors.toList());

        JavaRDD<Rating> dbRatings = sc.parallelize(dbRatingList);
        JavaRDD<Rating> fileRatings = lines.filter(line -> !line.equals(first)).map(new ParseRating());
        return fileRatings.union(dbRatings);
    }

    private Rating toRating(com.alon.main.server.entities.Rating rating) {
        return new Rating(rating.getUserId(), rating.getMovieId(), rating.getRating());
    }

    private double testModel(JavaRDD<Rating> ratings, MatrixFactorizationModel model) {


        JavaRDD<Tuple2<Object, Object>> userProducts = ratings.map(r -> new Tuple2<Object, Object>(r.user(), r.product()));

                // Evaluate the model on rating data
        JavaRDD<Tuple2<Tuple2<Integer, Integer>, Double>> predictionsNorPair = model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD().map(r -> new Tuple2<>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating()));
        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(predictionsNorPair);


        JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(ratings.map(r -> new Tuple2<>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating()))).join(predictions).values();;

        Double MSE = ratesAndPreds.mapToDouble(pair -> (pair._1() - pair._2()) * (pair._1() - pair._2())).mean();

        return MSE;

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


}