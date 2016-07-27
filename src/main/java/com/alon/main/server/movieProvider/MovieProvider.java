package com.alon.main.server.movieProvider;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.rest.RestImpl;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.alon.main.server.Const.Consts.BASE_YOU_TUBE_URL;
import static com.alon.main.server.Const.Consts.V_QUERY_PARAM;

/**
 * Created by alon_ss on 6/29/16.
 */
public class MovieProvider {

    private TmdbClient tmdbClient = new TmdbClient();
    private final static Logger logger = Logger.getLogger(MovieProvider.class);

    public CompletableFuture<Optional<String>> getYouTubeFutureTrailer(Movie movie){

//        URI url = UriBuilder.
//                fromUri(BASE_URL).
//                queryParam(V, KEY).
//                build();
        Optional<String> optionalTmdbId = movie.getExternalIds().stream().
                filter(external -> external.getSiteName().equals(MovieSite.TMDB)).map(ExternalId::getId).findFirst();

        if (!optionalTmdbId.isPresent()){
            logger.warn("Movie has no TmdbId. " + movie);
            return CompletableFuture.completedFuture(Optional.empty());
        }else{
            CompletableFuture<Optional<String>> futureString = tmdbClient.getFutureTrailer(optionalTmdbId.orElse(null));
            CompletableFuture<Optional<String>> futureUrl = futureString.
                    thenApply(x -> x.map(youTubeKey -> UriBuilder.fromUri(BASE_YOU_TUBE_URL).queryParam(V_QUERY_PARAM, youTubeKey).build().toString()));

            return futureUrl;
        }


    }

    public String getYouTubeTrailer(Movie movie){
        CompletableFuture<Optional<String>> youTubeFutureTrailer = getYouTubeFutureTrailer(movie);
        Optional<String> optionalUri;
        try {
            optionalUri = youTubeFutureTrailer.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            optionalUri = Optional.empty();
        }

        if (!optionalUri.isPresent()){
            logger.warn("Movie has no trailer. " + movie);
        }

        return optionalUri.orElse(null);
    }

}
