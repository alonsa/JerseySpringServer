package com.alon.test.server.service;

import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.YouTubeClient;
import com.alon.main.server.service.IntooiTVMockService;
import com.alon.main.server.service.MovieService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by alon_ss on 6/26/16.
 */

public class IntooiTVMockServiceTest {

    // This service is for tests purpose only and need to delete

    @InjectMocks
    private IntooiTVMockService testingObject;


    @Mock
    private MovieService movieService;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fillMovieDataFullMovie(){
        Movie movie = getMovie();
        movie.setLength(100L);

        Movie fullDetailedMovie = testingObject.fillMovieData(movie);
        Assert.assertEquals(fullDetailedMovie, movie);
    }

    @Test
    public void fillMovieDataNoUrilMovie(){

        Movie movie = getMovie();
        Movie fullDetailedMovie = testingObject.fillMovieData(movie);


//        if (movie.getLength() == null){
//            Optional<URI> optionalUri = convertToEmbedUri(movie.getUri());
//            URI uri  = optionalUri.orElse(getDefaultUri(movie));
//            movie.setUri(uri);
//            optionalUri.ifPresent(x -> setLengthFromYouTube(movie));
//
//            movieService.saveMovie(movie);
//        }
//        return movie;
    }

    private Movie getMovie() {
        Integer id = 1;
        String title = "mockMovie";
        List<String> genres = new ArrayList<>();
        List<ExternalId> externalIds = new ArrayList<>();

        return new Movie(id, title, genres, externalIds);
    }

    @Deprecated
    private void setLengthFromYouTube(Movie movie) {
        Optional<Long> optionalVodLength = Optional.empty();
        String youTubeId = (movie.getUri()).getPath().
                replace("https://", "").
                replace("www.youtube.com", "").
                replace("embed", "").
                replace("/", "");

        YouTubeClient youTubeClient = new YouTubeClient();
        optionalVodLength = youTubeClient.getVodLength(youTubeId);

        optionalVodLength.ifPresent(movie::setLength);
    }

    @Deprecated
    private Optional<URI> convertToEmbedUri(URI uri){
        Optional<URI> response = Optional.empty();
        if (uri != null){
            URI newUri = UriBuilder.
                    fromPath(uri.getHost()).
                    path("embed").
                    path(getYouTubeId(uri)).
                    queryParam("autoplay", 1).
                    build();

            response = Optional.ofNullable(newUri);

        }
        return response;
    }

    @Deprecated
    private String getYouTubeId(URI uri){
        String youTubeId = null;
        if (uri != null){
            youTubeId = uri.getQuery().replace("v=", "");
        }
        return youTubeId;
    }


    @Deprecated
    private URI getDefaultUri(Movie movie) {

        Map<URI, Long> urisToLength = new HashMap<>();
        try {
            urisToLength.put(new URI("https://www.youtube.com/embed/LTgRm6Qgscc?autoplay=1"), 25000L);
            urisToLength.put(new URI("https://www.youtube.com/embed/DhNMHcRSNdo?autoplay=1"), 13000L);
            urisToLength.put(new URI("https://www.youtube.com/embed/hvha-7EvwNg?autoplay=1"), 31000L);
            urisToLength.put(new URI("https://www.youtube.com/embed/kvg9GxWjgIw?autoplay=1"), 14000L);
            urisToLength.put(new URI("https://www.youtube.com/embed/OT9HsNszYCI?autoplay=1"), 15000L);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        int index = (Math.abs(movie.hashCode()) % urisToLength.size());

        List<URI> urls = new ArrayList<>();

        urls.addAll(urisToLength.keySet());
        URI defaultUri = urls.get(index);
        Long defaultUriLength = urisToLength.get(defaultUri);

        movie.setLength(defaultUriLength);

        return defaultUri;
    }


}