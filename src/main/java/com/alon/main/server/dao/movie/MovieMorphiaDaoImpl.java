package com.alon.main.server.dao.movie;

import com.alon.main.server.dao.MorphiaDao;
import com.alon.main.server.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public final class MovieMorphiaDaoImpl extends MorphiaDao<Movie> {

    @Override
    protected Class<Movie> getTypeClass() {
        return Movie.class;
    }

    @Override
    protected String getDbName() {
        return "vod";
    }

}