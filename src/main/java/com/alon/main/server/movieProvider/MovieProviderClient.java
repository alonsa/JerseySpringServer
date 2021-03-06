package com.alon.main.server.movieProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by alon_ss on 6/29/16.
 */
interface MovieProviderClient {

    CompletableFuture<Optional<String>> getFutureTrailer(String vodId);
    Optional<String> getFutureOverview(String vodId) throws ExecutionException, InterruptedException;
    Optional<String> getTrailer(String vodId);
}
