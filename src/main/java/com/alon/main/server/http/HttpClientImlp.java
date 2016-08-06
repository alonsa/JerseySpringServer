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
@Service
public class HttpClientImlp implements HttpClient{

    private final static Logger logger = Logger.getLogger(HttpClientImlp.class);

    private static AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

    public CompletableFuture<Response> call(String url){

        logger.debug("Make async http call to: " + url);
        return asyncHttpClient
                .prepareGet(url)
                .execute().toCompletableFuture();
    }

}
