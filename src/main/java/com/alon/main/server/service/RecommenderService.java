package com.alon.main.server.service;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.Rating;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface RecommenderService {

    @Deprecated  // need to delete this - only use for in memory movieDao.
    JavaSparkContext getJavaSparkContext();
    List<Integer> recommend(Integer user, Integer recommendationNumber);
    void updateModel(List<Rating> newRatings);
}