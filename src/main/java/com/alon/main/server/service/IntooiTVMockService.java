package com.alon.main.server.service;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.MovieProvider;
import com.alon.main.server.movieProvider.TmdbClient;
import com.alon.main.server.movieProvider.YouTubeClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
@Deprecated
public class IntooiTVMockService {

    // This service is for tests purpose only and need to delete
    private final static Logger logger = Logger.getLogger(IntooiTVMockService.class);

    @Autowired
    private MovieService movieService;

    TmdbClient tmdbClient = new TmdbClient();

    @Deprecated
    public Movie fillMovieData(Movie movie){

//        if (movie.getLength() == null){
            Optional<URI> optionalUri = convertToEmbedUri(movie.getUri());
            URI uri  = optionalUri.orElse(getDefaultUri(movie));
            movie.setUri(uri);
            optionalUri.ifPresent(x -> setLengthFromYouTube(movie));

//            movieService.saveMovie(movie);
//        }

        if (movie.getPlot() == null){
            Optional<String> optionalTmdbId = movie.getExternalIds().stream().
                    filter(external -> external.getSiteName().equals(MovieSite.TMDB)).map(ExternalId::getId).findFirst();

            if (!optionalTmdbId.isPresent()){
                logger.debug("Movie has no TmdbId. " + movie);

            }else{
                CompletableFuture<Optional<String>> futureString = tmdbClient.getFutureOverview(optionalTmdbId.orElse(null));
                Optional<String> overviewOption = Optional.empty();
                try {
                    overviewOption = futureString.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                movie.setPlot(overviewOption.orElse(null));
                movieService.saveMovie(movie);
            }
        }
        return movie;
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

        Map<URI, Long> urisToLength = getUrisToLengthMap();

        int index = (Math.abs(movie.hashCode()) % urisToLength.size());

        List<URI> urls = new ArrayList<>();

        urls.addAll(urisToLength.keySet());
        URI defaultUri = urls.get(index);
        Long defaultUriLength = urisToLength.get(defaultUri);

        movie.setLength(defaultUriLength);

        return defaultUri;
    }

    public Map<URI, Long> getUrisToLengthMap(){
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
        return urisToLength;
    }


}