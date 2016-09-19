package com.alon.main.server.service;

import com.alon.main.server.entities.Rating;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface RecommenderService {

    List<Integer> recommend(Integer user, Integer recommendationNumber);
    void updateModel(List<Rating> newRatings);
}