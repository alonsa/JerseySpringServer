package com.alon.test.server.service;

import com.alon.main.server.entities.*;
import com.alon.main.server.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bson.types.ObjectId;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by alon_ss on 7/31/16.
 */
public class RestApiServiceTest {

    @InjectMocks
    private RestApiService testingObject;

    @Mock
    private UserService userService;

    @Mock
    private RecommenderService recommenderService;

    @Mock
    private RatingService ratingService;

    @Mock
    private MovieService movieService;

    private Integer nextRecommendationSize = 5;
    private Integer responseSize = 2;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(testingObject, "nextRecommendationSize", nextRecommendationSize);
    }

    @Test
    public void testGetUser() throws Exception {

        User responseUser = new User(1, "test");
        when(userService.getUserByName(any())).thenReturn(responseUser);

        User response = testingObject.getUser("test");
        Assert.assertEquals(responseUser, response);
    }

    @Test
    public void testGetEpgRecommendationEmptyNextVodsNoCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(0, Optional.empty(), true);
    }

    @Test
    public void testGetEpgRecommendationNextVodsLessThenResponseSizeNoCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(1, Optional.empty(), true);
    }

    @Test
    public void testGetEpgRecommendationNextVodsEqualToResponseSizeNoCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(2, Optional.empty(), true);
    }

    @Test
    public void testGetEpgRecommendationNextVodsBiggerThenResponseSizeNoCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(4, Optional.empty(), true);
    }

    @Test
    public void testGetEpgRecommendationEmptyNextVodsWithCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(0, Optional.of(new ObjectId()), true);
    }

    @Test
    public void testGetEpgRecommendationNextVodsLessThenResponseSizeWithCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(1, Optional.of(new ObjectId()), true);
    }

    @Test
    public void testGetEpgRecommendationNextVodsEqualToResponseSizeWithCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(2, Optional.of(new ObjectId()), true);
    }

    @Test
    public void testGetEpgRecommendationNextVodsBiggerThenResponseSizeWithCurrentlyPlayAndLikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(4, Optional.of(new ObjectId()), true);
    }

    @Test
    public void testGetEpgRecommendationEmptyNextVodsNoCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(0, Optional.empty(), false);
    }

    @Test
    public void testGetEpgRecommendationNextVodsLessThenResponseSizeNoCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(1, Optional.empty(), false);
    }

    @Test
    public void testGetEpgRecommendationNextVodsEqualToResponseSizeNoCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(2, Optional.empty(), false);
    }

    @Test
    public void testGetEpgRecommendationNextVodsBiggerThenResponseSizeNoCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(4, Optional.empty(), false);
    }

    @Test
    public void testGetEpgRecommendationEmptyNextVodsWithCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(0, Optional.of(new ObjectId()), false);
    }

    @Test
    public void testGetEpgRecommendationNextVodsLessThenResponseSizeWithCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(1, Optional.of(new ObjectId()), false);
    }

    @Test
    public void testGetEpgRecommendationNextVodsEqualToResponseSizeWithCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(2, Optional.of(new ObjectId()), false);
    }

    @Test
    public void testGetEpgRecommendationNextVodsBiggerThenResponseSizeWithCurrentlyPlayAndDislikeCurrentlyPlay() throws Exception {
        testGetEpgRecommendation(4, Optional.of(new ObjectId()), false);
    }

    private void testGetEpgRecommendation(Integer userNextVodsSize, Optional<ObjectId> currentlyPlayOption, Boolean likeCurrentlyPlay) {
        List<Movie> movies = getMovies(userNextVodsSize);
        User testUser = new User(1, "test");

        testUser.addToNextVodList(movies.stream().map(BaseEntity::getId).collect(Collectors.toList()));

        // For recommendation
        Integer recommendationsSize = nextRecommendationSize + testUser.getRecentlyWatch().size() + testUser.getNextVods().size();
        List<Movie> recommendedVods = getMovies(recommendationsSize);
        List<Integer> recommendedInnerId = recommendedVods.stream().map(RecommandEntity::getInnerId).collect(Collectors.toList());
        when(recommenderService.recommend(any(), anyInt())).thenReturn(recommendedInnerId);
        when(movieService.getByInnerIds(any())).thenReturn(recommendedVods);

        ArrayList<Movie> dbMovies = Lists.newArrayList();

        if (likeCurrentlyPlay){
            currentlyPlayOption.map(currentlyPlay -> {
                Movie movie = new Movie(90210, "title", null, null);
                movie.setId(currentlyPlay);
                return movie;
            }).ifPresent(dbMovies::add);
        }
        dbMovies.addAll(movies);
        dbMovies.addAll(recommendedVods);
        when(movieService.getByIds(any())).thenReturn(dbMovies.subList(0, responseSize));

        List<Movie> response = testingObject.getEpgRecommendationForUser(testUser, currentlyPlayOption, likeCurrentlyPlay, responseSize);

        Integer currentlyPlayNumber = currentlyPlayOption.map(x -> likeCurrentlyPlay ? 1 : 0).orElse(0);

        Assert.assertEquals(response.size(), responseSize.intValue());
        Assert.assertNotNull(testUser.getCurrentlyWatch());
        Assert.assertEquals(testUser.getCurrentlyWatch().getMovieId(), response.get(0).getId());

        if (userNextVodsSize + currentlyPlayNumber >= responseSize){
            Assert.assertEquals(testUser.getNextVods().size(), userNextVodsSize - responseSize + currentlyPlayNumber);
        }else {
//            Assert.assertEquals(testUser.getNextVods().size(), 5);
            Assert.assertEquals(testUser.getNextVods().size(), nextRecommendationSize - responseSize + 2 *userNextVodsSize + currentlyPlayNumber);

        }
        currentlyPlayOption.ifPresent(currentlyPlay -> Assert.assertTrue(testUser.getRecentlyWatch().contains(currentlyPlay)));
        if (likeCurrentlyPlay){
            Assert.assertEquals(testUser.getCurrentlyWatch().getMovieId(), response.get(0).getId());
            currentlyPlayOption.ifPresent(currentlyPlay -> Assert.assertEquals(testUser.getCurrentlyWatch().getMovieId(), currentlyPlay));
            currentlyPlayOption.ifPresent(currentlyPlay -> Assert.assertTrue(response.stream().map(BaseEntity::getId).collect(Collectors.toList()).contains(currentlyPlay)));
        }
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, true, true, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, true, true, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, true, true, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, true, true, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, false, true, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, false, true, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, false, true, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, false, true, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, true, false, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, true, false, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, true, false, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, true, false, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, false, false, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, false, false, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, false, false, true);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithoutSleepLikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, false, false, true);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, true, true, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, true, true, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, true, true, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, true, true, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, false, true, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, false, true, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, false, true, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, false, true, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, true, false, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, true, false, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, true, false, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, true, false, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(true, currentlyPlayOptional, false, false, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithCurrentlyPlayOptionalWithoutLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.of(new ObjectId());
        doBackgroundJobTrest(false, currentlyPlayOptional, false, false, false);
    }

    @Test
    public void testDoBackgroundJobWithCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(true, currentlyPlayOptional, false, false, false);
    }

    @Test
    public void testDoBackgroundJobWithoutCurrentlyWatchWithoutCurrentlyPlayOptionalWithoutLengthWithoutSleepDislikeCurrentlyPlay() throws Exception {
        Optional<ObjectId> currentlyPlayOptional = Optional.empty();
        doBackgroundJobTrest(false, currentlyPlayOptional, false, false, false);
    }

    private void doBackgroundJobTrest(Boolean withCurrentlyWatch, Optional<ObjectId> currentlyPlayOptional, Boolean withLength, Boolean withSleep, Boolean isLikeCurrentlyPlay) throws InterruptedException, java.util.concurrent.ExecutionException {
        User testUser = new User(1, "test"); // With getCurrentlyWatch, without

        testUser.setId(new ObjectId());
        testUser.setInnerId(1);

        Movie movie = new Movie(1, "title", null, null);
        movie.setId(new ObjectId());
        if (withLength){
            movie.setLength(1L);
        }

        Set<ObjectId> expectedRatings = Sets.newHashSet();
        currentlyPlayOptional.ifPresent(expectedRatings::add);
        if (withCurrentlyWatch && movie.getLength() != null){
            expectedRatings.add(movie.getId());
        }

        when(movieService.getById(any())).thenReturn(movie);

        if (withCurrentlyWatch){
            CurrentlyWatch currentlyWatch = new CurrentlyWatch(movie.getId());
            testUser.setCurrentlyWatch(currentlyWatch);
        }
        Optional<ObjectId> currentlyWatchOption = Optional.ofNullable(testUser.getCurrentlyWatch()).map(CurrentlyWatch::getMovieId);

        if (withSleep){
            Thread.sleep(50);
        }

        User oldUser = new User();
        oldUser.setId(testUser.getId());
        oldUser.setCurrentlyWatch(testUser.getCurrentlyWatch());
        oldUser.setInnerId(testUser.getInnerId());
        oldUser.setName(testUser.getName());
        oldUser.setNextVods(Lists.newArrayList(testUser.getNextVods()));
        oldUser.setRecentlyWatch(Lists.newArrayList(testUser.getRecentlyWatch()));

        Set<ObjectId> oldRecentlyWatch = Sets.newHashSet(testUser.getRecentlyWatch());
        currentlyWatchOption.ifPresent(oldRecentlyWatch::add);

        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, currentlyWatchOption, currentlyPlayOptional, isLikeCurrentlyPlay);
        List<Rating> ratings = ratingsFuture.get();

        Assert.assertEquals(testUser, oldUser);
        Assert.assertEquals(Sets.newHashSet(testUser.getRecentlyWatch()), oldRecentlyWatch);
        Assert.assertEquals(ratings.size(), expectedRatings.size());
    }

    @Test
    public void testDoBackgroundJobNoCurrentlyPlayLikeAndCurrentlyWatchNoLengthOld() throws Exception {
        User testUser = new User(1, "test");

        Movie movie = new Movie(1, "title", null, null);
        movie.setId(new ObjectId());
        when(movieService.getById(any())).thenReturn(movie);

        CurrentlyWatch currentlyWatch = new CurrentlyWatch(movie.getId());
        testUser.setCurrentlyWatch(currentlyWatch);

        Optional<ObjectId> currentlyWatchOption = Optional.ofNullable(testUser.getCurrentlyWatch()).map(CurrentlyWatch::getMovieId);

        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, currentlyWatchOption, Optional.empty(), true);
        List<Rating> ratings = ratingsFuture.get();

        Assert.assertTrue(ratings.isEmpty());
        Assert.assertEquals(testUser.getRecentlyWatch().size(), 1);
        Assert.assertEquals(testUser.getRecentlyWatch().get(0), movie.getId());
    }

    @Test
    public void testDoBackgroundJobNoCurrentlyPlayLikeAndCurrentlyWatchWithPause() throws Exception {
        User testUser = new User(1, "test");
        testUser.setInnerId(1);

        Movie movie = new Movie(1, "title", null, null);
        movie.setId(new ObjectId());
        movie.setLength(1L);
        when(movieService.getById(any())).thenReturn(movie);

        CurrentlyWatch currentlyWatch = new CurrentlyWatch(movie.getId());
        testUser.setCurrentlyWatch(currentlyWatch);

        Optional<ObjectId> currentlyWatchOption = Optional.ofNullable(testUser.getCurrentlyWatch()).map(CurrentlyWatch::getMovieId);

        Thread.sleep(50);
        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, currentlyWatchOption, Optional.empty(), true);
        List<Rating> ratings = ratingsFuture.get();


        Assert.assertEquals(ratings.size(), 1);
        Assert.assertTrue(ratings.get(0).getRating() > 0);
        Assert.assertEquals(testUser.getRecentlyWatch().size(), 1);
        Assert.assertEquals(testUser.getRecentlyWatch().get(0), movie.getId());
    }

    @Test
    public void testDoBackgroundJobNoCurrentlyPlayLikeAndCurrentlyWatch() throws Exception {
        User testUser = new User(1, "test");
        testUser.setInnerId(1);

        Movie movie = new Movie(1, "title", null, null);
        movie.setId(new ObjectId());
        movie.setLength(10L);
        when(movieService.getById(any())).thenReturn(movie);

        CurrentlyWatch currentlyWatch = new CurrentlyWatch(movie.getId());
        testUser.setCurrentlyWatch(currentlyWatch);

        Optional<ObjectId> currentlyWatchOption = Optional.ofNullable(testUser.getCurrentlyWatch()).map(CurrentlyWatch::getMovieId);

        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, currentlyWatchOption, Optional.empty(), true);
        List<Rating> ratings = ratingsFuture.get();

        Assert.assertEquals(ratings.size(), 1);
        Assert.assertEquals(ratings.get(0).getRating(), 0.0);
        Assert.assertEquals(testUser.getRecentlyWatch().size(), 1);
        Assert.assertEquals(testUser.getRecentlyWatch().get(0), movie.getId());
    }

    @Test
    public void testDoBackgroundJobNoCurrentlyPlayAndLike() throws Exception {
        User testUser = new User(1, "test");
        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, Optional.empty(), Optional.empty(), true);
        List<Rating> ratings = ratingsFuture.get();

        Assert.assertTrue(ratings.isEmpty());
        Assert.assertTrue(testUser.getRecentlyWatch().isEmpty());
    }

    @Test
    public void testDoBackgroundJobNoCurrentlyPlayAndDisLike() throws Exception {
        User testUser = new User(1, "test");
        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, Optional.empty(), Optional.empty(), false);
        List<Rating> ratings = ratingsFuture.get();

        Assert.assertTrue(ratings.isEmpty());
        Assert.assertTrue(testUser.getRecentlyWatch().isEmpty());
    }

    @Test
    public void testDoBackgroundJobCurrentlyPlayAndLike() throws Exception {
        User testUser = new User(1, "test");
        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, Optional.empty(), Optional.of(new ObjectId()), true);

        List<Rating> ratings = ratingsFuture.get();

        Assert.assertTrue(ratings.isEmpty());
        Assert.assertTrue(testUser.getRecentlyWatch().isEmpty());
    }

    @Test
    public void testDoBackgroundJobCurrentlyPlayAndDisLike() throws Exception {
        User testUser = new User(1, "test");
        Future<List<Rating>> ratingsFuture = testingObject.doBackgroundJob(testUser, Optional.empty(), Optional.of(new ObjectId()), false);

        List<Rating> ratings = ratingsFuture.get();

        Assert.assertTrue(ratings.isEmpty());
        Assert.assertTrue(testUser.getRecentlyWatch().isEmpty());
    }

    private List<Movie> getMovies(int num) {

        List<Movie> vods = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Movie movie = new Movie(i, "title", null, null);
            movie.setId(new ObjectId());
            vods.add(movie);
        }
        return vods;
    }

}