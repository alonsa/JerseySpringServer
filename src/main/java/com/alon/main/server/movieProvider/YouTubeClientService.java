package com.alon.main.server.movieProvider;

import com.alon.main.server.Const.YouTubeOrderEnum;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;

import java.util.*;

/**
 * Created by alon_ss on 7/7/16.
 */
interface YouTubeClientService {

    Set<Movie> getVideoDetails(Set<String> videoIds);
    Optional<Movie> getVideoDetails(String videoId);
    Optional<ContentProvider> getContentProviderByChannelId(String channelId);
    Set<String> getContentProviderVodIds(String youTubeId, Long lastDataFetch, Optional<Integer> maxSize);
    List<String> getContentProviderMostPopularVodIds(String youTubeId, Long lastDataFetch, Optional<Integer> maxSize);
    List<String> getRelatedVideos(String videoId, String chanelId);

}
