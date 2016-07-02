package com.alon.main.server.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
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
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public final class RecommenderService {


    private JavaSparkContext sc;
    private MatrixFactorizationModel model;

    @PostConstruct
    @Async
    private void init() {
        SparkConf sparkConf = new SparkConf().setAppName("JavaALS");
        sparkConf.setMaster("local[2]").set("spark.executor.memory", "1g");
        sc = new JavaSparkContext(sparkConf);
        setModel();
    }

    @Deprecated  // need to delete this - only use for in memory movieDao.
    public JavaSparkContext getJavaSparkContext(){
        return sc;
    }

    @PreDestroy
    private void destroy() {
        sc.stop();
    }

//    https://github.com/apache/spark/tree/master/examples/src/main/java/org/apache/spark/examples/mllib

    public List<Integer> recommend(Integer user) {
        Rating[] ratings = model.recommendProducts(user, RECENTLY_WATCH_MAX_SIZE + RESPONSE_NUM);
        List<Rating> ratingsList = Arrays.asList(ratings);
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
            model = MatrixFactorizationModel.load(sc.sc(), MODEL_PATH);
        } catch (Exception e) {
            model = calculateAndCreateModel();
        }
    }

    private MatrixFactorizationModel calculateAndCreateModel() {

        int rank = 10;
        int iterations = 10;
        int blocks = -1;

//        if (args.length == 5) {
//            blocks = Integer.parseInt(args[4]);
//        }

        JavaRDD<String> lines = sc.textFile(RATINGS_PATH);
        String first = lines.first();
        JavaRDD<Rating> ratings = lines.filter(line -> !line.equals(first)).map(new ParseRating());

        MatrixFactorizationModel newModel = ALS.train(ratings.rdd(), rank, iterations, 0.01, blocks);
        newModel.save(sc.sc(), MODEL_PATH);

        deleteAndAddAll(newModel);

        return newModel;
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

        deleteAndAddDir(model, conf, USER_FEATURES_PATH);

        deleteAndAddDir(model, conf, PRODUCT_FEATURES_PATH);

    }

    private static void deleteAndAddDir(MatrixFactorizationModel model, Configuration conf, String pathString) {
        Path path = new Path(pathString);
        try {

            FileSystem.get(conf).delete(path, true);
            model.userFeatures().toJavaRDD().map(RecommenderService::FeaturesToString).saveAsTextFile(pathString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String FeaturesToString(Tuple2<Object, double[]> feature) {
        return feature._1() + "," + Arrays.toString(feature._2());
    }


}