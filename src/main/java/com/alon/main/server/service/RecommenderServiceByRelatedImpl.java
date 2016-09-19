package com.alon.main.server.service;

import com.alon.main.server.entities.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class RecommenderServiceByRelatedImpl implements RecommenderService{

    private final static Logger logger = Logger.getLogger(RecommenderServiceByRelatedImpl.class);

    private UserService userService;
    private MovieService movieService;

    @Autowired
    public RecommenderServiceByRelatedImpl(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @Override
    public List<Integer> recommend(Integer userInnerId, Integer recommendationNumber) {

        User user = userService.getByInnerId(userInnerId);

        if (user == null){
            return Lists.newArrayList();
        }

        // Get related From user
        List<ObjectId> recentlyWatch = user.getRecentlyWatch();
        Set<ObjectId> recentlyWatchSet = Sets.newHashSet(user.getRecentlyWatch());

        // Get Vods from DB
        List<Movie> recentlyWatchVods = movieService.getByIds(recentlyWatch);

        // Map Vods to ObjectId and Vod Map
        Map<ObjectId, Movie> recentlyWatchIdToVod = recentlyWatchVods.stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        // Get a list of related Ids
        List<ObjectId> relatedIds = recentlyWatchIdToVod.values().stream().map(Movie::getRelatedVods).flatMap(Collection::stream).collect(Collectors.toList());

        // Group related Ids by appearance
        Map<ObjectId, Long> relatedIdsToCounts =
                relatedIds.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Sort related Ids by appearance AND filter the not recently watch
        List<ObjectId> recommendedIds = relatedIdsToCounts.entrySet().stream()
                .sorted(Map.Entry.<ObjectId, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .filter(x -> !recentlyWatchSet.contains(x))
                .collect(Collectors.toList());

        // Get recommended Vods from DB
        List<Movie> recommendedVods = movieService.getByIds(recommendedIds);

        // Map recommended Vods to ObjectId and Vod Map
        Map<ObjectId, Movie> recommendedIdToVod = recommendedVods.stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        List<Integer> recommendedInnerIds = recommendedIds.stream()
                .map(x -> Optional.ofNullable(recommendedIdToVod.get(x)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(RecommandEntity::getInnerId)
                .collect(Collectors.toList());

        return recommendedInnerIds;
    }

    @Override
    public void updateModel(List<Rating> newRatings) {

    }


}