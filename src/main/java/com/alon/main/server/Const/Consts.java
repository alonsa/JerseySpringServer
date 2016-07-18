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
    public static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";
    public static final String TMDB_APP_ID = "api_key";
    public static final String TMDB_VIDEOS = "videos";
    public static final String TMDB_MOVIE = "movie";
    public static final String TMDB_KEY = "9426e5f190c68f947f6d8768a8cc04e8";


    //You Tube
    public static final String YOU_TUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/";
    public static final String YOU_TUBE_VIDEOS = "videos";
    public static final String YOU_TUBE_MOVIE = "movie";
    public static final String YOU_TUBE_PART = "part";
    public static final String YOU_TUBE_CONTENT_DETAILS = "contentDetails";
    public static final String YOU_TUBE_ID = "id";
    public static final String YOU_TUBE_KEY = "key";
    public static final String YOU_TUBE_PRIVATE_KEY = "AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA";


    // Recommander
    public static final Pattern COMMA = Pattern.compile(",");

    // BaseDao
    private static final String BASE_PATH = "/Users/alon_ss/Learning/Recommender/ml-latest";
    public static final String MOVIES_PATH = BASE_PATH + "/movies.csv";
    public static final Pattern VERTICAL_BAR = Pattern.compile("\\|");
    public static final String HOST = "localhost";
    public static final Integer PORT = 27017;
    public static final String VOD_DB = "vod";
    public static final String USER_DB = "user";
    public static final String COUNTER_DB = "counter";
    public static final String RATING_DB = "rating";

    // BaseEntity
    public static final String ID_FIELD = "_id";
    public static final String INNER_ID_FIELD = "innerId";

    // Movie
    public static final String URI_FIELD = "uri";

    // User
    public static final String NAME_FIELD = "name";

    // Counter
    public static final String ENTITY_NAME_FIELD = "entityName";
    public static final String VALUE_FIELD = "value";

    // Player
    public static final Integer RECENTLY_WATCH_MAX_SIZE = 4;

    //Web
    public static final String RESPONSE_NUM = "2";






}
