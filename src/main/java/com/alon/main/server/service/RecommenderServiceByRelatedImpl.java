package com.alon.main.server.service;

import com.alon.main.server.entities.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
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
    private ContentProviderService contentProviderService;

    @Autowired
    public RecommenderServiceByRelatedImpl(UserService userService, MovieService movieService, ContentProviderService contentProviderService) {
        this.userService = userService;
        this.movieService = movieService;
        this.contentProviderService = contentProviderService;
    }

    @Override
    public List<Integer> recommend(Integer userInnerId, Integer recommendationNumber) {

        User user = userService.getByInnerId(userInnerId);

        if (user == null){
            return Lists.newArrayList();
        }

        List<Integer> recommendedInnerIds = null;

        if (user.getRecentlyWatch().isEmpty()){
            recommendedInnerIds = getContentProviderMostPopular(recommendationNumber, user);

        }else{
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

            recommendedInnerIds = recommendedIds.stream()
                    .map(x -> Optional.ofNullable(recommendedIdToVod.get(x)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(RecommandEntity::getInnerId)
                    .collect(Collectors.toList());


            if (recommendedInnerIds.size() < recommendationNumber){
                Set<Integer> contentProviderMostPopular = Sets.newHashSet(getContentProviderMostPopular(recommendationNumber, user));
                contentProviderMostPopular.removeAll(recommendedInnerIds);
                List<Integer> recentlyWatchVodsInnerId = recentlyWatchVods.stream().map(RecommandEntity::getInnerId).collect(Collectors.toList());
                contentProviderMostPopular.removeAll(recentlyWatchVodsInnerId);
                recommendedInnerIds.addAll(contentProviderMostPopular);
            }
        }

        return recommendedInnerIds;
    }

    private List<Integer> getContentProviderMostPopular(Integer recommendationNumber, User user) {
        List<Integer> recommendedInnerIds;ContentProvider contentProvider = contentProviderService.getById(user.getCpId());
        List<Movie> mostPopularVods = contentProviderService.getContentProviderMostPopular(contentProvider, recommendationNumber);
        recommendedInnerIds = mostPopularVods.stream().map(RecommandEntity::getInnerId).collect(Collectors.toList());
        return recommendedInnerIds;
    }

    @Override
    public void updateModel(List<Rating> newRatings) {

    }
}