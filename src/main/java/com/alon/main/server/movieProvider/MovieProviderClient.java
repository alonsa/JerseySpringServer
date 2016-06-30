package com.alon.main.server.movieProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by alon_ss on 6/29/16.
 */
interface MovieProviderClient {

    public CompletableFuture<Optional<String>> getFutureTrailer(String vodId);
    public Optional<String> getTrailer(String vodId);
}
