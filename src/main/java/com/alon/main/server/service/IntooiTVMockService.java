package com.alon.main.server.service;

import com.alon.main.server.entities.Movie;

import java.net.URI;

/**
 * Created by alon_ss on 6/26/16.
 */

@Deprecated
public interface IntooiTVMockService {

    Movie fillMovieData(Movie movie);
    Movie changeYouTubeUri(Movie movie);
    String getYouTubeId(URI uri);

}