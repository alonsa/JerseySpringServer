package com.alon.main.server.service;

import com.alon.main.server.dao.Dao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alon.main.server.Const.Consts.RESPONSE_NUM;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class UserService {

    @Autowired
    public RecommenderService recommenderService;

    @Autowired
    public Dao<Movie> movieDao;

//    @Autowired
//    public Dao<User> userDao;
//
//    public List<Movie> recommendForUser(ObjectId userId){
////        User user = userDao.getById(userId);
//        List<Integer> movieIds = recommenderService.recommend(user.getInnerId());
//        List<Movie> movies = movieDao.getByInnerIds(movieIds);
//        CircularFifoQueue<Integer> recentlyWatch = user.getRecentlyWatch();
//        Stream<Movie> movieStream = movies.stream().filter(movie -> recentlyWatch.contains(movie.getInnerId())).limit(RESPONSE_NUM);
//        List<Movie> recommendMovies = movieStream.collect(Collectors.toList());
//
//        // just for demo
//        movieStream.forEach(movie ->  user.addToRecentlyWatch(movie.getInnerId()));
//
//        return recommendMovies;
//    }



}