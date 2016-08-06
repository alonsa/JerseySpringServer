package com.alon.main.server.service;

import com.alon.main.server.dao.movie.MovieDao;
import com.alon.main.server.entities.Movie;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class MovieServiceImpl implements MovieService{

    private final static Logger logger = Logger.getLogger(MovieServiceImpl.class);

    @PostConstruct
    private void init() {
        logger.debug("###############################");
        logger.debug("###   MovieService is up!   ###");
        logger.debug("###############################");
    }

    @Autowired
    public MovieDao movieDao;

    public List<Movie> getByInnerIds(List<Integer> moviesInnerIds) {
        return movieDao.getOrderedByInnerIds(moviesInnerIds);
    }

    public void saveMovie(Movie movie){
        movieDao.save(movie);
    }

    public List<Movie> getByIds(List<ObjectId> moviesInnerIds) {
        return movieDao.getOrderedByIds(moviesInnerIds);
    }

    public Movie getById(ObjectId movieId) {
        return movieDao.getById(movieId);
    }


}