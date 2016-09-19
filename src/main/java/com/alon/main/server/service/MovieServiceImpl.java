package com.alon.main.server.service;

import com.alon.main.server.Const.MovieSite;
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
public class MovieServiceImpl extends RecommendEntityServiceImpl<Movie> implements MovieService{

    private final static Logger logger = Logger.getLogger(MovieServiceImpl.class);

    @PostConstruct
    private void init() {
        logger.debug("###############################");
        logger.debug("###   MovieService is up!   ###");
        logger.debug("###############################");
    }

    @Autowired
    public MovieDao movieDao;

//    @Override
//    public void save(Movie entity) {
//        movieDao.save(entity);
//    }

//    @Override
//    public void saveAll(List<Movie> list) {
//        movieDao.saveAll(list);
//    }

//    @Override
//    public void saveMovie(Movie movie){
//        movieDao.save(movie);
//    }
//
//    @Override
//    public void saveMovies(List<Movie> movies) {
//        movieDao.saveAll(movies);
//    }

    @Override
    public List<Movie> getByIds(List<ObjectId> moviesInnerIds) {
        return movieDao.getOrderedByIds(moviesInnerIds);
    }

    @Override
    public Movie getById(ObjectId movieId) {
        return movieDao.getById(movieId);
    }

    @Override
    public Movie findByExternalSiteId(MovieSite site, String externalId){
        return movieDao.findByExternalSiteId(site, externalId);
    }

    @Override
    public List<Movie> findByExternalSiteIds(MovieSite site, Iterable<String> externalId){
        return movieDao.findByExternalSiteIds(site, externalId);
    }

}