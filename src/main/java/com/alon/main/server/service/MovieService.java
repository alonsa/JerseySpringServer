package com.alon.main.server.service;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.Movie;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface MovieService extends RecommendEntityService<Movie> {

    List<Movie> getByIds(List<ObjectId> moviesInnerIds);
    Movie getById(ObjectId movieId);

    Movie findByExternalSiteId(MovieSite site, String externalId);
    List<Movie> findByExternalSiteIds(MovieSite site, Iterable<String> externalId);
}