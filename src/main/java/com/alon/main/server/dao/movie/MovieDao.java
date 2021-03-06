package com.alon.main.server.dao.movie;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.dao.MorphiaRecommandDao;
import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.entities.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alon.main.server.Const.Consts.VOD_DB;

/**
 * Created by alon_ss on 6/26/16.
 */
public interface MovieDao extends RecommandDao<Movie> {

    List<Movie> findByExternalSiteIds(MovieSite site, Iterable<String> externalId);
    Movie findByExternalSiteId(MovieSite site, String externalId);
}