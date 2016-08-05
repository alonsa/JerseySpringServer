package com.alon.main.server.service;

import com.alon.main.server.util.Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.COMMA;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class RecommenderService {

    private final static Logger logger = Logger.getLogger(RecommenderService.class);

    private final RatingService ratingService;

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

    private final static Integer LIKE_PREFIX = 99900000;
    private final static Integer UNLIKE_PREFIX = 66600000;
    private final static int MB = 1024*1024;

    @Autowired
    public RecommenderService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostConstruct
    private void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SparkConf sparkConf = new SparkConf().setAppName("JavaALS");
        String master = "local[" + threadNumber + "]";
        String executorMemory = executorMemorySize +"g";
        String driverMemory = driverMemorySize +"g";

        sparkConf.
                setMaster(master).
                set("spark.executor.memory", executorMemory);

        if (StringUtils.isNumeric(driverMemorySize)){
            sparkConf.set("spark.driver.memory", driverMemory);
        }

        if (testMode){
            sparkConf.set("spark.testing", "true");
        }

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        logger.debug("########################################");
        logger.debug("###   Starting RecommenderService    ###");
        logger.debug("###   " + master + "                       ###");
        logger.debug("### spark.executor.memory: " + executorMemorySize + "         ###");

        if (StringUtils.isNumeric(driverMemorySize)){
            logger.debug("### spark.driver.memory: " + driverMemorySize + "    ###");
        }

        logger.debug("### Heap utilization statistics [MB] ###");
        logger.debug("### Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / MB + "                  ###");
        logger.debug("### Free Memory:" + runtime.freeMemory() / MB + "                 ###");
        logger.debug("### Total Memory:" + runtime.totalMemory() / MB + "                ###");
        logger.debug("### Max Memory:" + runtime.maxMemory() / MB + "                  ###");
        logger.debug("### Service up time : " + Util.millisecondDurationToDate(stopWatch.getTime()) + " ###");
        logger.debug("#################################");

        sc = new JavaSparkContext(sparkConf);
        logger.debug("Set Model");
        setModel();
        logger.debug("Load Rating From File");
        loadRatingFromFile();
        logger.debug("Set Default Movie List");
        setDefaultMovieList();

        stopWatch.stop();
        logger.debug("########################################");
        logger.debug("###   RecommenderService             ###");
        logger.debug("### Service up time : " + Util.millisecondDurationToDate(stopWatch.getTime()) + " ###");
        logger.debug("#################################");
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

    @Async
    public void updateModel(List<Rating> newRatings) {

        logger.debug("Update model for rating: " + newRatings);

        if (newRatings.isEmpty()){
            int user = newRatings.get(0).user();
            List<Integer> ratingUserNums = newRatings.stream().
                    map(r -> new Tuple2<>(r.product(), r.rating() > 3 ? LIKE_PREFIX : UNLIKE_PREFIX)).
                    map(t -> t._1() + t._2()).
                    collect(Collectors.toList());

            MatrixFactorizationModel model = modelContainer.getReadModel();
            RDD<Tuple2<Object, double[]>> scalaUserFeatures = model.userFeatures();
            modelContainer.returnReadModel();

            JavaRDD<Tuple2<Object, double[]>> userFeatures = scalaUserFeatures.toJavaRDD();
            List<double[]> userVector = JavaPairRDD.fromJavaRDD(userFeatures).lookup(user);

            List<double[]> newUserVector = new ArrayList<>();
            newUserVector.addAll(userVector);

            for (Integer ratingUserNum: ratingUserNums) {
                List<double[]> ratingUser = JavaPairRDD.fromJavaRDD(userFeatures).lookup(ratingUserNum);
                newUserVector = addVectors(newUserVector, ratingUser, null);
            }

            List<Tuple2<Object, double[]>> newUserList = new ArrayList<>();
            newUserList.add(new Tuple2<>(user, newUserVector.get(0)));
            JavaRDD<Tuple2<Object, double[]>> newUserRdd = sc.parallelize(newUserList);
            JavaRDD<Tuple2<Object, double[]>> combinedRdd = userFeatures.filter(x -> !x._1().equals(user)).union(newUserRdd);

            MatrixFactorizationModel newModel = new MatrixFactorizationModel(model.rank(), combinedRdd.rdd(), model.productFeatures());
            modelContainer.setModel(newModel, false);
        }
    }

    private List<double[]> addVectors(List<double[]> originVector, List<double[]> newVector, Integer num) {

        if (newVector.isEmpty()){
            return originVector;
        }

        double[] userVector67List = new double[originVector.get(0).length];
        int retio = 1;
        if (num != null){
            retio = 1 / (num + 1);
        }

        for (int i = 0; i < 10; i++) {
            Double pluse = originVector.get(0)[i] + (newVector.get(0)[i] * retio);
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

        JavaRDD<Rating> loveIt = fileRatings.groupBy(Rating::product).keys().map(new ParseLikeRatingFromProduct());
        JavaRDD<Rating> hateIt = fileRatings.groupBy(Rating::product).keys().map(new ParseUnLikeRatingFromProduct());

        JavaRDD<Rating> dbRatings = sc.parallelize(dbRatingList);
        return fileRatings.
                union(dbRatings).
                union(loveIt).
                union(hateIt).
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

    private static class ParseUnLikeRatingFromProduct implements Function<Integer, Rating> {

        @Override
        public Rating call(Integer product) {
            return new Rating(UNLIKE_PREFIX + product, product, 0);
        }
    }

    private static class ParseLikeRatingFromProduct implements Function<Integer, Rating> {

        @Override
        public Rating call(Integer product) {
            return new Rating(LIKE_PREFIX + product, product, 5);
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
            logger.debug("TRY TO ACQUIRE READ LOCK");
            lock.readLock().lock();
            logger.debug("ACQUIRE READ LOCK SUCCESS!!!");

            return model;
        }

        void returnReadModel(){
            logger.debug("TRY TO RELEASE READ LOCK");
            lock.readLock().unlock();
            logger.debug("RELEASE READ LOCK SUCCESS!!!");

        }

        private void WriteLockModel(){
            logger.debug("TRY TO ACQUIRE WRITE LOCK");

            lock.writeLock().lock();
            logger.debug("WRITE LOCK SUCCESS!!!");

        }

        private void returnWriteModel(){
            logger.debug("TRY TO RELEASE WRITE LOCK");
            lock.writeLock().unlock();
            logger.debug("RELEASE WRITE LOCK SUCCESS!!!");

        }
    }


}