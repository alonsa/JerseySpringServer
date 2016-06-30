package com.alon.main.server.movieProvider;

import com.alon.main.server.entities.Movie;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.alon.main.server.Const.Consts.BASE_YOU_TUBE_URL;
import static com.alon.main.server.Const.Consts.V_QUERY_PARAM;

/**
 * Created by alon_ss on 6/29/16.
 */
public class MovieProvider {


    private TmdbClient tmdbClient = new TmdbClient();

    public CompletableFuture<Optional<URI>> getYouTubeFutureTrailer(Movie movie){

//        URI url = UriBuilder.
//                fromUri(BASE_URL).
//                queryParam(V, KEY).
//                build();
        CompletableFuture<Optional<String>> futureKey = tmdbClient.getFutureTrailer(movie.getTmdbId());
        CompletableFuture<Optional<URI>> futureUrl = futureKey.
                thenApply(x -> x.map(youTubeKey -> UriBuilder.fromUri(BASE_YOU_TUBE_URL).queryParam(V_QUERY_PARAM, youTubeKey).build()));
        return futureUrl;
    }

    public String getYouTubeTrailer(Movie movie){
        Optional<String> trailer = tmdbClient.getTrailer(movie.getTmdbId());

        return trailer.orElse(null);
    }

}
