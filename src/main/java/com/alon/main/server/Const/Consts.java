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
    public static final String YOU_TUBE_SEARCH = "search";
    public static final String YOU_TUBE_MOVIE = "movie";
    public static final String YOU_TUBE_PART = "part";
    public static final String YOU_TUBE_CONTENT_DETAILS = "contentDetails";
    public static final String YOU_TUBE_SNIPPET = "snippet";
    public static final String YOU_TUBE_ID = "id";
    public static final String YOU_TUBE_CHANNEL_ID = "channelId";
    public static final String YOU_TUBE_KEY = "key";
    public static final String YOU_TUBE_MAX_RESULTS = "maxResults";
    public static final String YOU_TUBE_TYPE = "type";
    public static final String YOU_TUBE_CHANNEL = "channel";
    public static final String YOU_TUBE_VIDEO = "video";
    public static final String YOU_TUBE_VIDEO_ID = "videoId";
    public static final String YOU_TUBE_PUBLISHED_AFTER = "publishedAfter";
    public static final String YOU_TUBE_ORDER = "order";
    public static final String YOU_TUBE_DATE = "date";
    public static final String YOU_TUBE_PAGE_TOKEN = "pageToken";
    public static final String YOU_TUBE_PRIVATE_KEY = "AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA";
    public static final String YOU_TUBE_ITEMS = "items";
    public static final String YOU_TUBE_TITLE = "title";
    public static final String YOU_TUBE_PUBLISHED_AT = "publishedAt";
    public static final String YOU_TUBE_DESCRIPTION = "description";
    public static final String YOU_TUBE_DURATION = "duration";
    public static final String YOU_TUBE_REGION_RESTRICTION = "regionRestriction";
    public static final String YOU_TUBE_CHANNEL_TITLE = "channelTitle";
    public static final String YOU_TUBE_NEXT_PAGE_TOKEN = "nextPageToken";
    public static final String YOU_TUBE_RELATED_TO_VIDEO_ID = "relatedToVideoId";
    public static final String COMMA = ",";
    public static final int YOU_TUBE_MAX_RESULTS_VAL = 50;

    // Recommander
    public static final Pattern COMMA_PATTERN = Pattern.compile(",");

    // BaseDao
    private static final String BASE_PATH = "/Users/alon_ss/Learning/Recommender/ml-latest";
    public static final String MOVIES_PATH = BASE_PATH + "/movies.csv";
    public static final Pattern VERTICAL_BAR = Pattern.compile("\\|");
    public static final String HOST = "localhost";
    public static final Integer PORT = 27017;
    public static final String VOD_DB = "vod";
    public static final String CONTENT_PROVIDER_DB = "contentProvider";
    public static final String USER_DB = "user";
    public static final String COUNTER_DB = "counter";
    public static final String RATING_DB = "rating";

    // BaseEntity
    public static final String ID_FIELD = "_id";
    public static final String INNER_ID_FIELD = "innerId";

    // ContentProviderEntity
    public static final String CP_ID_FIELD = "cpId";
    public static final String YOU_TUBE_ID_FIELD = "youTubeId";

    // Movie
    public static final String URI_FIELD = "uri";
    public static final String EXTERNAL_SITE_TO_ID_FIELD = "externalSiteToId";

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
