package com.alon.main.server.service;

import com.alon.main.server.dao.movie.MovieMorphiaDaoImpl;
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
public class MovieService {

    private final static Logger logger = Logger.getLogger(MovieService.class);

    @PostConstruct
    private void init() {
        logger.debug("###############################");
        logger.debug("###   MovieService is up!   ###");
        logger.debug("###############################");
    }

    @Autowired
    public RecommenderService recommenderService;

    @Autowired
    public MovieMorphiaDaoImpl movieBaseDao;

    public List<Movie> getByInnerIds(List<Integer> moviesInnerIds) {
        return movieBaseDao.getByInnerIds(moviesInnerIds);
    }

    public void saveMovie(Movie movie){
        movieBaseDao.save(movie);
    }

    public Movie getById(ObjectId movieId) {
        return movieBaseDao.getById(movieId);
    }


}