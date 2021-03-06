package com.alon.main.server.service;

import com.alon.main.server.Const.RecommendedLogicEnum;
import com.alon.main.server.entities.*;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class EpgServiceImpl implements EpgService {

    private final static Logger logger = Logger.getLogger(EpgServiceImpl.class);

    private Map<String, RecommenderService> recommenders = Maps.newHashMap();

    private UserService userService;

    private RatingService ratingService;

    private MovieService movieService;

    private RecommenderService defaultRecommenderService;

    private ContentProviderService contentProviderService;

    @Value("${user.next.recommendation.size}")
    private Integer nextRecommendationSize;

    @Autowired
    public EpgServiceImpl(
            RatingService ratingService,
            MovieService movieService,
            UserService userService,
            ContentProviderService contentProviderService,
            ApplicationContext applicationContext,
            @Qualifier("recommenderServiceByRelatedImpl") RecommenderService defaultRecommenderService)
    {
        this.ratingService = ratingService;
        this.movieService = movieService;
        this.userService = userService;
        this.contentProviderService = contentProviderService;
        if (applicationContext != null){
            recommenders = applicationContext.getBeansOfType(RecommenderService.class);
        }

        logger.debug("#################################");
        logger.debug("###   EpgService is up!   ###");
        logger.debug("#################################");
        this.defaultRecommenderService = defaultRecommenderService;
    }

    @Async
    public Future<List<Rating>> doBackgroundJob(
            User user,
            Optional<ObjectId> currentlyWatchOptional,
            Optional<ObjectId> currentlyPlayOptional,
            Boolean isLikeCurrentlyPlay
    ) {
        logger.debug("RestApiServiceImpl, doBackgroundJob: " + Thread.currentThread().getName());
        Optional<Movie> optionalPlayMovie = currentlyPlayOptional.map(movieService::getById);
        logger.debug("Playing movie:" + optionalPlayMovie);

        // Add rating by the user action (want to play or unlike the asset)
        logger.debug("Add rating by the user action (want to play or unlike the asset)");
        Double likeRating = isLikeCurrentlyPlay ? 5.0 : 0.0;

        List<Rating> newRatings = new ArrayList<>();
        Optional<Rating> optionalPlayRating = optionalPlayMovie.map(movie -> new Rating(user.getInnerId(), movie.getInnerId(), likeRating));
        optionalPlayRating.ifPresent(newRatings::add);

        // Add rating by the time the user watched the asset
        logger.debug("Add rating by the time the user watched the asset");
        CurrentlyWatch currentlyWatch = user.getCurrentlyWatch();
        if (currentlyWatchOptional.isPresent()){
            ObjectId movieId = currentlyWatchOptional.get();

            Movie movie = movieService.getById(movieId);
            if (movie != null && movie.getLength() != null && movie.getInnerId() != null){
                Instant startWatchTime = Instant.ofEpochMilli(currentlyWatch.getStartWatchTime());
                Duration watchedDuration = Duration.between(startWatchTime, Instant.now()); // duration of watched vod

                Double watchingPersentage = ((double) watchedDuration.toMillis() / (movie.getLength().doubleValue() * 1000)) * 5;

                double normalRating = ((watchingPersentage > 5) ? 5.0 : new BigDecimal(watchingPersentage).setScale(1, RoundingMode.HALF_UP).doubleValue());

                Rating rating = new Rating(user.getInnerId(), movie.getInnerId(), normalRating);
                newRatings.add(rating);
            }
        }

        if (!newRatings.isEmpty()){
            List<Rating> checkedRatings = newRatings.stream().filter(Rating::isValid).collect(Collectors.toList());
            ratingService.addRatings(checkedRatings);
            getRecommenderService(user).updateModel(checkedRatings);
        }

        return new AsyncResult<>(newRatings);
    }

    public User getUser(String userName){

        logger.debug("Look for user: " + userName);
        User user = userService.getUserByName(userName);
        logger.debug("Found user:" + user);

        return user;
    }

    public List<Movie> getEpgRecommendationForUser(
            User user,
            Optional<ObjectId> currentlyPlayOption,
            boolean likeCurrentlyPlay,
            Integer recommandNum){

        if (currentlyPlayOption.isPresent()){
            ObjectId currentlyPlay = currentlyPlayOption.get();
            user.getRecentlyWatch().remove(currentlyPlay);
            user.getRecentlyWatch().add(currentlyPlay);

            user.getNextVods().remove(currentlyPlay);
            if (likeCurrentlyPlay){
                user.addToHeadNextVodList(currentlyPlay);
            }
        }

        if (user.getNextVods().size() < recommandNum){
            fillNextRecommendationUserList(user, Optional.empty());
        }

        List<Movie> moviesList = getMoviesFromUserData(user, recommandNum);

        if (recommandNum.equals(moviesList.size())){
            moviesList.stream().findFirst().
                    map(BaseEntity::getId).
                    map(CurrentlyWatch::new).
                    ifPresent(user::setCurrentlyWatch);

            userService.save(user);
        }

        return moviesList;
    }

    private List<Movie> getMoviesFromUserData(User user, Integer recommandNum) {
        List<ObjectId> vodIds = user.pullFromNextVodList(recommandNum);
        logger.debug("Ask DB for recommendedMoviesInnerIds by innerIds: " + vodIds);
        List<Movie> dbMovies = movieService.getByIds(vodIds);
        logger.debug("DB movies response: " + dbMovies);
        return dbMovies;
    }

    private void fillNextRecommendationUserList(User user, Optional<Integer> extandRecommandNumOption) {

        Integer recommendationNumber = user.getRecentlyWatch().size() + user.getNextVods().size() +
                nextRecommendationSize + extandRecommandNumOption.orElse(0);

        HashSet<ObjectId> userRecentlyWatchSet = new HashSet<>(user.getRecentlyWatch());
        HashSet<ObjectId> userNextVodsSet = new HashSet<>(user.getNextVods());

        List<Movie> recommendedMovies = getFromRecommendationService(user, recommendationNumber);
        List<ObjectId> filterRecommendedMovieIds = recommendedMovies.stream().
                filter(movie -> !userRecentlyWatchSet.contains(movie.getId())).
                filter(movie -> !userNextVodsSet.contains(movie.getId())).
                map(BaseEntity::getId).
                collect(Collectors.toList());

        user.addToNextVodList(filterRecommendedMovieIds);
    }

    private List<Movie> getFromRecommendationService(User user, Integer recommandNum) {
        // Get recommended vods Ids
        List<Integer> recommendedMoviesInnerIds = getRecommenderService(user).recommend(user.getInnerId(), recommandNum);
        logger.debug("Recommended vods Ids: " +  recommendedMoviesInnerIds);

        // Get the movies by the Ids
        logger.debug("Ask DB for recommendedMoviesInnerIds by innerIds: " + recommendedMoviesInnerIds);
        List<Movie> dbMovies = movieService.getByInnerIds(recommendedMoviesInnerIds);
        logger.debug("DB movies response: " + dbMovies);

        // Create a Map of ides to movies
        Map<Integer, Movie> innerIdToMovie = dbMovies.stream().collect(Collectors.toMap(Movie::getInnerId, Function.identity()));

        // Map the movie ids to movies - while keep the order
        return recommendedMoviesInnerIds.stream().map(innerIdToMovie::get).collect(Collectors.toList());
    }

    private RecommenderService getRecommenderService(User user){
        ContentProvider contentProviderDB = contentProviderService.getById(user.getCpId());
        RecommendedLogicEnum recommendedLogicEnum = Optional.ofNullable(contentProviderDB)
                .map(ContentProvider::getRecomenderLogic)
                .flatMap(Optional::ofNullable)
                .orElse(RecommendedLogicEnum.RELATED);

        return recommenders.getOrDefault(recommendedLogicEnum.toString(), defaultRecommenderService);
    }
}