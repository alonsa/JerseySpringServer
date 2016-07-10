package com.alon.main.server.service;

import com.alon.main.server.dao.movie.MovieMorphiaDaoImpl;
import com.alon.main.server.dao.rating.RatingMorphiaDaoImpl;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class RatingService {

    @Autowired
    public RatingMorphiaDaoImpl ratingDao;

    public List<Rating> getAllToList() {
        return ratingDao.getAllToList();
    }


}