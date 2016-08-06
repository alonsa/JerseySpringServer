package com.alon.main.server.service;

import com.alon.main.server.entities.Rating;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface RatingService {

    List<Rating> getAllToList();
    void addRatings(List<Rating> newRatings);
}