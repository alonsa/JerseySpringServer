package com.alon.main.server.http;

import org.apache.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Created by alon_ss on 6/29/16.
 */
public interface  HttpClient {
    public CompletableFuture<Response> call(String url);
}
