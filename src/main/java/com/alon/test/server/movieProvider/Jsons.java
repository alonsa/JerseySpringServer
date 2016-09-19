package com.alon.test.server.movieProvider;

/**
 * Created by alon_ss on 9/11/16.
 */
public class Jsons {

    public static String videoDetailsFullJsonString = "{\n" +
            " \"kind\": \"youtube#videoListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/t1oK67iNgYH4QOkylRcb3KJfZT4\\\"\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 1,\n" +
            "  \"resultsPerPage\": 1\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#video\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/UJrff4iZEL-11XQgV4emRIG_esc\\\"\",\n" +
            "   \"id\": \"d4HO9UY9Mb0\",\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T20:07:49.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"title\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     },\n" +
            "     \"standard\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/sddefault.jpg\",\n" +
            "      \"width\": 640,\n" +
            "      \"height\": 480\n" +
            "     },\n" +
            "     \"maxres\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/maxresdefault.jpg\",\n" +
            "      \"width\": 1280,\n" +
            "      \"height\": 720\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"categoryId\": \"23\",\n" +
            "    \"liveBroadcastContent\": \"none\",\n" +
            "    \"localized\": {\n" +
            "     \"title\": \"title\",\n" +
            "     \"description\": \"\"\n" +
            "    }\n" +
            "   },\n" +
            "   \"contentDetails\": {\n" +
            "    \"duration\": \"PT2M12S\",\n" +
            "    \"dimension\": \"2d\",\n" +
            "    \"definition\": \"hd\",\n" +
            "    \"caption\": \"false\",\n" +
            "    \"licensedContent\": false,\n" +
            "    \"projection\": \"rectangular\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static String vodLengthTestForNoItemsJsonString = "{\n" +
            " \"kind\": \"youtube#videoListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/t1oK67iNgYH4QOkylRcb3KJfZT4\\\"\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 1,\n" +
            "  \"resultsPerPage\": 1\n" +
            " },\n" +
            "}";

    public static String ContentProviderByChannelIdJsonString = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/S5lizbxwglE-ggVWWlIeHjqMpr8\\\"\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 1,\n" +
            "  \"resultsPerPage\": 5\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/Kzt1xWZR22yCJsDXaHQiIdSWam8\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#channel\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2011-10-08T18:09:56.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"channelTitle\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://yt3.ggpht.com/-bt4robf2x78/AAAAAAAAAAI/AAAAAAAAAAA/CenaNmzSgnI/s88-c-k-no-mo-rj-c0xffffff/photo.jpg\"\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://yt3.ggpht.com/-bt4robf2x78/AAAAAAAAAAI/AAAAAAAAAAA/CenaNmzSgnI/s240-c-k-no-mo-rj-c0xffffff/photo.jpg\"\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://yt3.ggpht.com/-bt4robf2x78/AAAAAAAAAAI/AAAAAAAAAAA/CenaNmzSgnI/s240-c-k-no-mo-rj-c0xffffff/photo.jpg\"\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"channelTitle\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static String ContentProviderByChannelIdNoSnippetJsonString = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/S5lizbxwglE-ggVWWlIeHjqMpr8\\\"\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 1,\n" +
            "  \"resultsPerPage\": 5\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/Kzt1xWZR22yCJsDXaHQiIdSWam8\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#channel\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\"\n" +
            "   },\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static String ContentProviderByChannelIdNoItemsJsonString = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/S5lizbxwglE-ggVWWlIeHjqMpr8\\\"\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 1,\n" +
            "  \"resultsPerPage\": 5\n" +
            " },\n" +
            "}";

    public static String ChannelVodsForFullDataJsonString = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/V_jYh_MQCSQbhfMF5A3TNUexlEs\\\"\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 5,\n" +
            "  \"resultsPerPage\": 1\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/fCJYH9OzteW9-TXPH2iQcD5jbXk\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T18:39:04.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר קליפ מקוצר\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static String ChannelVodsForSlittedDataJsonString1 = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/E1E48drn75jJTq5u9xV1Q13STe0\\\"\",\n" +
            " \"nextPageToken\": \"CAMQAA\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 5,\n" +
            "  \"resultsPerPage\": 3\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/fCJYH9OzteW9-TXPH2iQcD5jbXk\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T18:39:04.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר קליפ מקוצר\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/BmGIAiAI0gkV_078LD7d7BCLCsI\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"5PLx-kNkVxA\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-15T07:24:32.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר-קליפ חתונה\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/5PLx-kNkVxA/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/5PLx-kNkVxA/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/5PLx-kNkVxA/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/_qVuEEDI3h-6j3KgfrotscRUGy4\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"d4HO9UY9Mb0\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T20:07:49.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר  פלאש מוב\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/d4HO9UY9Mb0/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static  String ChannelVodsForSlittedDataJsonString2 = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/koNfDhZDN1Turu96lczk0A0AqmY\\\"\",\n" +
            " \"prevPageToken\": \"CAMQAQ\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 5,\n" +
            "  \"resultsPerPage\": 3\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/HGgPQijvMTD8OwNQN-1GM0hf5v4\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"sp6n6bOxyPo\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2013-06-04T18:48:48.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"יום סבבא חטיבה צעירה תשעג\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/sp6n6bOxyPo/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/sp6n6bOxyPo/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/sp6n6bOxyPo/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/lguNMuFq7D7QX0WrRY0KUbq6qMQ\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"18RXuWMyHNI\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2013-06-04T19:37:19.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"הכנת דמויות ביום סבבא חטיבה צעירה תשעג\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/18RXuWMyHNI/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/18RXuWMyHNI/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/18RXuWMyHNI/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static  String filterJsonObjectString = "{\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/fCJYH9OzteW9-TXPH2iQcD5jbXk\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T18:39:04.000Z\",\n" +
            "    \"channelId\": \"testChannelId\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר קליפ מקוצר\",\n" +
            "    \"description\": \"\",\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }";

    public static  String filterJsonObjectNoValueJsonString = "{\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/fCJYH9OzteW9-TXPH2iQcD5jbXk\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T18:39:04.000Z\",\n" +
            "    \"channelId\": \"testChannelId2\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר קליפ מקוצר\",\n" +
            "    \"description\": \"\",\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }";

    public static String filterJsonObjectNoChannelIdJsonString = "{\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/fCJYH9OzteW9-TXPH2iQcD5jbXk\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T18:39:04.000Z\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר קליפ מקוצר\",\n" +
            "    \"description\": \"\",\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }";

    public static String filterJsonObjectNoSnippetJsonString = "{\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/fCJYH9OzteW9-TXPH2iQcD5jbXk\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "  }";

    public static String emptyJsonString = "{}";

    public static String RelatedVideosJsonString1 = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/twXjx71ufedp5E1FYV1H9RJaeNI\\\"\",\n" +
            " \"nextPageToken\": \"CAUQAA\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 53,\n" +
            "  \"resultsPerPage\": 5\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/y3Loq1a-x26BCPdHwqDH10lC1rU\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"hgxeKy-CQFU\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-14T10:13:21.000Z\",\n" +
            "    \"channelId\": \"UC2s0g1THlE4WgL0bcD0Opcw\",\n" +
            "    \"title\": \"קליפ חתונה שירה וגל\",\n" +
            "    \"description\": \"תיאור\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/hgxeKy-CQFU/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/hgxeKy-CQFU/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/hgxeKy-CQFU/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"יקיר וקנין\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/gZrNjzqmd6cFN5v6UIHGiszYPFE\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"oSzf70g9T1s\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-06-01T13:36:11.000Z\",\n" +
            "    \"channelId\": \"UCizJzVvGLhgbiVMS8H3GUyQ\",\n" +
            "    \"title\": \"כתבתו של הילד שניצח למרות הכל\",\n" +
            "    \"description\": \"עד שהוא נולד אף אחד לא ידע כמה אתגרים מחכים לו בחיים, הוא לא רצה לוותר ובכל שלב ושלב בחייו נלחם על מנת להיות שווה. אברהם אפלקר הילד שניצח למרות הכל. כתבתם של מרב מלחי,ירדן קשטן ומתנאל לוסטיג.\\nלפרטים נוספים מרב מלחי- 0546222447\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/oSzf70g9T1s/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/oSzf70g9T1s/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/oSzf70g9T1s/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"Merav Malchi\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/z6E-d860gwlnG6GXR_pEuNhcSKM\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"7aG50_Gg6oE\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-17T16:41:04.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר קליפ מקוצר\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/7aG50_Gg6oE/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/luJY5d8ARvPp759BRcLSRRARxcg\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"5PLx-kNkVxA\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-15T05:47:28.000Z\",\n" +
            "    \"channelId\": \"UCbcovN-W9s9bkoe0wqIEjGg\",\n" +
            "    \"title\": \"אברהם ושיר אפלקר-קליפ חתונה\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/5PLx-kNkVxA/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/5PLx-kNkVxA/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/5PLx-kNkVxA/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיר אפלקר-דרוק\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/v_1F6VyyjdFxrZxqxphgyyfILT4\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"kaj8hXgDnIM\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2014-02-02T20:48:22.000Z\",\n" +
            "    \"channelId\": \"UC_g2M5ISZy50qERgv5ktDZQ\",\n" +
            "    \"title\": \"הצעת נישואין בגראנד קניון באר שבע אסף&נעמה- פלאש-מוב\",\n" +
            "    \"description\": \"נעמה בהלם מוחלט אחרי שצופה מהצד ולא יודעת שכל ההפקה תוכננה במיוחד בשבילה!\\nעל הטבעת- אסף אוקנין. כוריאוגרפיה ורקדנים של דן אודיז. צילום ועריכה: Lychee\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/kaj8hXgDnIM/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/kaj8hXgDnIM/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/kaj8hXgDnIM/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"Dan Odiz\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    public static String RelatedVideosJsonString2 = "{\n" +
            " \"kind\": \"youtube#searchListResponse\",\n" +
            " \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/dDQpUBC2avSFavHP6EJhavEaZ0c\\\"\",\n" +
            " \"prevPageToken\": \"CAUQAQ\",\n" +
            " \"regionCode\": \"IL\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 53,\n" +
            "  \"resultsPerPage\": 5\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/pUKKIO4zNnqVqZ_Y8RRdzj8SQ8U\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"fTWHJ36ER8k\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-04-03T17:17:01.000Z\",\n" +
            "    \"channelId\": \"UCrD_HzNzhSLyyZwKhKbHKGw\",\n" +
            "    \"title\": \"קליפ חתונה שלומי והודיה\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/fTWHJ36ER8k/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/fTWHJ36ER8k/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/fTWHJ36ER8k/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"הודיה דביר\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/WMaTDrDQK3o9GNRWREUeNL0UUhI\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"w8r90B2bX2M\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2010-09-07T20:20:51.000Z\",\n" +
            "    \"channelId\": \"UCRC-2bZcpYYmOwJjD5c6Y1A\",\n" +
            "    \"title\": \"פלאש מוב הגדול בישראל - לוטו - גרסת דאנס  ISRAEL Flash Mob HD\",\n" +
            "    \"description\": \"לאחר יותר ממליון צפיות ברחבי העולם, הנה גרסת הדאנס החדשה של הפלאש מוב הגדול בישראל. \\nמאות רקדנים פשטו בהפתעה על חוף ראשון לציון ופתחו בריקוד המוני כחלק מהקמפיין החדש של הלוטו - מפעל הפיס.\\nהאירוע הופק על ידי קבוצת מיתוס:\\nמנכ\\\"ל גדי דקל, מפיקה לירן גולדשטיין, מפיק טכני דניאל קורן\\nביים ג'קי בכר, כוריאוגרפיה של לימור שלב\\nעוזרת כראוגרפיה אורטל ג'אן, עריכה מוזיקלית ישי רזיאל\\nאת הוידאו קליפ \\\"פלאש מוב\\\" הפיקה החברה של גלעד עדין:\\nביים רם גיל, והפיקה דורין בורנובסקי\\nשימו לב - סרט ה Making-Of יעלה בקרוב לרשת...\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/w8r90B2bX2M/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/w8r90B2bX2M/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/w8r90B2bX2M/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"Gilad Adin\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/MqUy2P1tYZ1R2lH3OrfK3yF3wKs\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"JzfIZdi5ZPA\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2015-09-21T12:54:09.000Z\",\n" +
            "    \"channelId\": \"UCfwh9F1Aw2Qhj_ipvqUFqAA\",\n" +
            "    \"title\": \"משה ורחלי הקליפ | פלאשבק צלמים | 0543000310\",\n" +
            "    \"description\": \"פלאשבק צילום אירועים.\\n\\nלהזמנת צילום 054-3000-310 ישראל\\nהצטרפו לדף הפייסבוק - https://www.facebook.com/pages/%D7%A4%D7%9C%D7%90%D7%A9%D7%91%D7%A7-%D7%A6%D7%9C%D7%9E%D7%99%D7%9D/988226821211819?fref=ts\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/JzfIZdi5ZPA/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/JzfIZdi5ZPA/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/JzfIZdi5ZPA/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"פלאשבק צלמים\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/Nk4GEYJS3knoZIreip8tpzG9tXw\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"K0qR-WCyj2Y\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-02-13T17:22:47.000Z\",\n" +
            "    \"channelId\": \"UCn-JiVJTkKCpW9FDboZ4BUw\",\n" +
            "    \"title\": \"קליפ חתונה יונדב ושיר\",\n" +
            "    \"description\": \"קליפ חתונה יונדב ושיר\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/K0qR-WCyj2Y/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/K0qR-WCyj2Y/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/K0qR-WCyj2Y/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"שיראל משען\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/bhp32mawrq4si74cSai9wKKr9hM\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"MjmsTZxkGQw\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-02T22:48:39.000Z\",\n" +
            "    \"channelId\": \"UCfwh9F1Aw2Qhj_ipvqUFqAA\",\n" +
            "    \"title\": \"קליפ חתונה בסגנון ייחודי מבית פלאשבק צלמים | 0543000310\",\n" +
            "    \"description\": \"פלאשבק צילום אירועים.\\n\\nלהזמנת צילום 054-3000-310 ישראל\\nהצטרפו לדף הפייסבוק - https://www.facebook.com/pages/%D7%A4%D7%9C%D7%90%D7%A9%D7%91%D7%A7-%D7%A6%D7%9C%D7%9E%D7%99%D7%9D/988226821211819?fref=ts\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/MjmsTZxkGQw/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/MjmsTZxkGQw/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/MjmsTZxkGQw/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"פלאשבק צלמים\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/xYMT_iP-kauEeUMt-21MyVeGOK4\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"FpQyHUha_dA\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-01-09T10:38:48.000Z\",\n" +
            "    \"channelId\": \"UC8Unt2PIjwmeOYVWsDhUm8Q\",\n" +
            "    \"title\": \"פלאש מוב חתונה - עמית ואלינה\",\n" +
            "    \"description\": \"פלאש מוב חתונה - עמית ואלינה\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/FpQyHUha_dA/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/FpQyHUha_dA/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/FpQyHUha_dA/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"sheffer85\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/dH7P1e-FUxzw_YZEbr6NUe3lz2k\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"eCetdYrkctQ\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2016-03-31T10:19:26.000Z\",\n" +
            "    \"channelId\": \"UC_4SYUpF-tnh8pdOgSpycwQ\",\n" +
            "    \"title\": \"פלאש מוב חתונה דוד וליטל!!!! :)\",\n" +
            "    \"description\": \"פלאש מוב חתונה דוד וליטל :)\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/eCetdYrkctQ/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/eCetdYrkctQ/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/eCetdYrkctQ/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"רעות קורצברג\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/rLEJQSwE9bRD9oAs6jdMAzT6tsY\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"K-jo-ERosaY\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2015-09-29T07:56:46.000Z\",\n" +
            "    \"channelId\": \"UC0GgioZ1XWa9xNJJH66h7rg\",\n" +
            "    \"title\": \"היי סקול פסטיגל - מחרוזת מוקי\",\n" +
            "    \"description\": \"מחרוזת שירים של הכוכב האורח של היי סקול פסטיגל - מוקי\\n\\nבחנוכה הקרוב כל הכוכבים הכי גדולים מגיעים לתיכון גל:\\nיהודה לוי, אליאנה תדהר, שחר חסון, שירן סנדל, עומר דרור, אנה אהרונוב, דניאל מורשת, אורנה דץ, נוה צור, אריה מוסקונה, אושרת אינגשט, תותי ניניו, ניר חלפון\\nכוכב אורח: מוקי\\n\\nהיכנסו לפייסבוק של HOT VOD Young\\nhttps://www.facebook.com/hotvodyoung\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/K-jo-ERosaY/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/K-jo-ERosaY/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/K-jo-ERosaY/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"HOTVODyoung\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/3z4RMWFzfCk2bk74FG6bYTidy_c\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"onzCP_-pI9M\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2015-06-25T19:34:00.000Z\",\n" +
            "    \"channelId\": \"UCyC-A0z3r7F5tdKYxdUwzwg\",\n" +
            "    \"title\": \"ריקוד פלאשמוב בחתונה של אביב ונטלי 25/06/2015\",\n" +
            "    \"description\": \"תודה לכל מי שהשתתף, הייתה כמות משוגעת של אנשים, הוד הדר פאר ושמחה ביער בחדרה\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/onzCP_-pI9M/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/onzCP_-pI9M/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/onzCP_-pI9M/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"tal7\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#searchResult\",\n" +
            "   \"etag\": \"\\\"I_8xdZu766_FSaexEaDXTIfEWc0/00KkVPTnC6lDNXHnaJK6pBLZmrc\\\"\",\n" +
            "   \"id\": {\n" +
            "    \"kind\": \"youtube#video\",\n" +
            "    \"videoId\": \"dk1dvNEBkVM\"\n" +
            "   },\n" +
            "   \"snippet\": {\n" +
            "    \"publishedAt\": \"2013-06-30T10:11:16.000Z\",\n" +
            "    \"channelId\": \"UC4B2y27Eyr7yO0bDc5vNT4g\",\n" +
            "    \"title\": \"פלאש מוב ריקוד החיים-LifeDance במתחם הרובע ראשון לציון\",\n" +
            "    \"description\": \"\",\n" +
            "    \"thumbnails\": {\n" +
            "     \"default\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/dk1dvNEBkVM/default.jpg\",\n" +
            "      \"width\": 120,\n" +
            "      \"height\": 90\n" +
            "     },\n" +
            "     \"medium\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/dk1dvNEBkVM/mqdefault.jpg\",\n" +
            "      \"width\": 320,\n" +
            "      \"height\": 180\n" +
            "     },\n" +
            "     \"high\": {\n" +
            "      \"url\": \"https://i.ytimg.com/vi/dk1dvNEBkVM/hqdefault.jpg\",\n" +
            "      \"width\": 480,\n" +
            "      \"height\": 360\n" +
            "     }\n" +
            "    },\n" +
            "    \"channelTitle\": \"Liraz Zur\",\n" +
            "    \"liveBroadcastContent\": \"none\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

}
