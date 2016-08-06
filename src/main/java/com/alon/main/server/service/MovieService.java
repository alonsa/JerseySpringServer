package com.alon.main.server.service;

import com.alon.main.server.entities.Movie;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface MovieService {

    List<Movie> getByInnerIds(List<Integer> moviesInnerIds);
    void saveMovie(Movie movie);
    List<Movie> getByIds(List<ObjectId> moviesInnerIds);
    Movie getById(ObjectId movieId);


}