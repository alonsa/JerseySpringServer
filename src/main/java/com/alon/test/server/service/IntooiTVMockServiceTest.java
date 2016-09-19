package com.alon.test.server.service;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.TmdbClientService;
import com.alon.main.server.movieProvider.YouTubeClientServiceImpl;
import com.alon.main.server.service.IntooiTVMockServiceImpl;
import com.alon.main.server.service.MovieService;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by alon_ss on 6/26/16.
 */

public class IntooiTVMockServiceTest {

    // This service is for tests purpose only and need to delete

    @InjectMocks
    private IntooiTVMockServiceImpl testingObject;

    @Mock
    private MovieService movieService;

    @Mock
    private TmdbClientService tmdbClient;

    @Mock
    private YouTubeClientServiceImpl youTubeClient;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getYouTubeIdWatched(){
        URI uri = null;

        try {
            uri = new URI("https://www.youtube.com/watch?v=j9xml1CxgXI");
        } catch ( URISyntaxException ignored) {}

        String id = testingObject.getYouTubeId(uri);
        Assert.assertEquals(id, "j9xml1CxgXI");
    }

    @Test
    public void getYouTubeIdNull(){
        String id = testingObject.getYouTubeId(null);
        Assert.assertNull(id);
    }

    @Test
    public void fillMovieDataFullMovie(){
        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(false);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
        testMovie(fullDetailedMovie);
    }

    @Test
    public void fillMovieDataFullMovieWithTmdbDataAndBadURL(){
        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(true);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));

        List<ExternalId> externalIds = Lists.newArrayList();
        ExternalId externalId = new ExternalId(MovieSite.TMDB, "8844");
        externalIds.add(externalId);
        movie.setExternalIds(externalIds);

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
        testMovie(fullDetailedMovie);
    }


    @Test
    public void fillMovieDataFullMovieWithTmdbDataAndGoodURL(){

        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(false);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));

        List<ExternalId> externalIds = Lists.newArrayList();
        ExternalId externalId = new ExternalId(MovieSite.TMDB, "8844");
        externalIds.add(externalId);
        movie.setExternalIds(externalIds);

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
        testMovie(fullDetailedMovie);
    }

//    @BeforeMethod
//    public void beforeFillMovieDataFullMovieWithTmdbDataAndURL(){
//        MockitoAnnotations.initMocks(this);
//        try {
//            when(tmdbClient.getFutureOverview(anyString())).thenReturn(Optional.of("mock overview: 666"));
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void fillMovieDataFullMovieWithTmdbDataAndURL() throws URISyntaxException, ExecutionException, InterruptedException {

        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(false);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));
        when(tmdbClient.getFutureOverview(anyString())).thenReturn(Optional.of("mock overview: 8844"));
//        doReturn(Optional.of("mock overview: 8844")).when(tmdbClient).getFutureOverview(anyString());


        movie.setUri(new URI("https://www.youtube.com/embed/LTgRm6Qgscc?autoplay=1"));
        List<ExternalId> externalIds = Lists.newArrayList();
        ExternalId externalId = new ExternalId(MovieSite.TMDB, "8844");
        externalIds.add(externalId);
        movie.setExternalIds(externalIds);

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
        testMovie(fullDetailedMovie);
    }

    @Test
    public void fillMovieDataFullMovieWithTmdbDataAndURLNoOverview() throws URISyntaxException, ExecutionException, InterruptedException {

        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(false);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));
        when(tmdbClient.getFutureOverview(any())).thenReturn(Optional.empty());

        movie.setUri(new URI("https://www.youtube.com/embed/LTgRm6Qgscc?autoplay=1"));
        List<ExternalId> externalIds = Lists.newArrayList();
        ExternalId externalId = new ExternalId(MovieSite.TMDB, "8844");
        externalIds.add(externalId);
        movie.setExternalIds(externalIds);

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
        testMovie(fullDetailedMovie);
    }

    @Test
    public void fillMovieDataFullMovieWithURLNoOverview() throws URISyntaxException, ExecutionException, InterruptedException {

        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(false);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));
        when(tmdbClient.getFutureOverview(any())).thenReturn(Optional.empty());

        movie.setUri(new URI("https://www.youtube.com/embed/LTgRm6Qgscc?autoplay=1"));

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
        testMovie(fullDetailedMovie);
    }

    @Test
    public void fillMovieDataNoUrilMovie(){
        Movie movie = getMovie();
        movie.setLength(100L);
        movie.setForbidden(false);
        when(youTubeClient.getVideoDetails(anyString())).thenReturn(Optional.of(movie));

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        testMovie(fullDetailedMovie);
    }

    private void testMovie(Movie movie){
        Assert.assertNotNull(movie.getPlot());
        Assert.assertNotNull(movie.getTitle());
        Assert.assertNotNull(movie.getLength());
        Assert.assertNotNull(movie.getUri());
    }

    private Movie getMovie() {
        Integer id = 1;
        String title = "mockMovie";
        List<String> genres = new ArrayList<>();
        List<ExternalId> externalIds = new ArrayList<>();
        Movie movie = new Movie(id, title, genres, externalIds);
        movie.setId(new ObjectId());
        return movie;
    }
}