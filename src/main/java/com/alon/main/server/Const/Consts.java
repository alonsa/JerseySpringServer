package com.alon.main.server.Const;

import java.util.regex.Pattern;

/**
 * Created by alon_ss on 6/12/16.
 */
public  class Consts {


    // YOU TUBE
    public static final String BASE_YOU_TUBE_URL = "https://www.youtube.com/watch";
    public static final String V_QUERY_PARAM = "v";

    //The movie DB
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String APP_ID = "api_key";
    public static final String VIDEOS = "videos";
    public static final String MOVIE = "movie";
    public static final String KEY = "9426e5f190c68f947f6d8768a8cc04e8";

    // Recommander
    public static final String OUTPUT_DIR = "/Users/alon_ss/Learning/Recommender/outputDir";
    public static final String USER_FEATURES_PATH = OUTPUT_DIR + "/userFeatures";
    public static final String PRODUCT_FEATURES_PATH = OUTPUT_DIR + "/productFeatures";
    public static final String BASE_PATH = "/Users/alon_ss/Learning/Recommender/ml-latest";
//    public static final String BASE_PATH = "/Users/alon_ss/Learning/Recommender/ml-latest-small";
    public static final String RATINGS_PATH = BASE_PATH + "/ratings.csv";
    public static final String MODEL_PATH = OUTPUT_DIR + "/model";
    public static final Pattern COMMA = Pattern.compile(",");

    // Dao
    public static final String MOVIES_PATH = BASE_PATH + "/movies.csv";
    public static final Pattern VERTICAL_BAR = Pattern.compile("\\|");

    // Player
    public static final Integer RECENTLY_WATCH_MAX_SIZE = 4;

    //Web
    public static final Integer RESPONSE_NUM = 2;






}
