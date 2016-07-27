package com.alon.main.server.service;

import com.alon.main.server.dao.rating.RatingMorphiaDaoImpl;
import com.alon.main.server.entities.CurrentlyWatch;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.Rating;
import com.alon.main.server.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
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


    public void addRating(User user) {
        CurrentlyWatch currentlyWatch = user.getCurrentlyWatch();
        if (currentlyWatch != null && currentlyWatch.getMovie() != null){
            Movie movie = currentlyWatch.getMovie();
            Instant startWatchTime = Instant.ofEpochMilli(currentlyWatch.getStartWatchTime());
            Duration watchedDuration = Duration.between(startWatchTime, Instant.now()); // duration of watched vod

            double watchingPersentage = (watchedDuration.toMillis() / movie.getLength().doubleValue()) * 5;
            double normalRating = ((watchingPersentage > 5) ? 5.0 : watchingPersentage);

            Rating rating = new Rating(user.getInnerId(), movie.getInnerId(), normalRating);
            ratingDao.save(rating);
        }
    }

    public Rating addRating(Integer user, Integer movie, Boolean like) {

        Double normalRating = like ? 5.0 : 0.0;
        Rating rating = new Rating(user, movie, normalRating);
        ratingDao.save(rating);

        return rating;

    }


}