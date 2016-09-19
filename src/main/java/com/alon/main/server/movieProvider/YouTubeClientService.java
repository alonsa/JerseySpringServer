package com.alon.main.server.movieProvider;

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
    Set<String> getContentProviderVods(ContentProvider contentProvider, Long lastDataFetch);
    List<String> getRelatedVideos(String videoId, String chanelId);

}
