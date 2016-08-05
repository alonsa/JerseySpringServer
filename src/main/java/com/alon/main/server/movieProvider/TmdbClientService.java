package com.alon.main.server.movieProvider;

import com.alon.main.server.http.HttpClient;
import org.apache.log4j.Logger;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/29/16.
 */
@Service
public class TmdbClientService implements MovieProviderClient {

    private final static Logger logger = Logger.getLogger(TmdbClientService.class);

    @PostConstruct
    private void init() {
        logger.debug("###################################");
        logger.debug("###   TmdbClientService is up!  ###");
        logger.debug("###################################");
    }

    @Override
    public CompletableFuture<Optional<String>> getFutureTrailer(String vodId) {

        if (vodId == null){
            return new CompletableFuture<>();
        }

        URI url = UriBuilder.
                fromUri(TMDB_BASE_URL).
                path(TMDB_MOVIE).
                path(vodId).
                path(TMDB_VIDEOS).
                queryParam(TMDB_APP_ID, TMDB_KEY).
                build();

        return HttpClient.call(url.toString()).
                thenApply(Response::getResponseBody).
                thenApply(JSONObject::new).
                thenApply(this::getLengthFromeJson).exceptionally(ex -> Optional.empty());

    }

    @Override
    public Optional<String> getFutureOverview(String vodId) throws ExecutionException, InterruptedException {

        if (vodId == null){
            return Optional.empty();
        }

        URI url = UriBuilder.
                fromUri(TMDB_BASE_URL).
                path(TMDB_MOVIE).
                path(vodId).
                queryParam(TMDB_APP_ID, TMDB_KEY).
                build();

        return HttpClient.call(url.toString()).
                thenApply(Response::getResponseBody).
                thenApply(JSONObject::new).
                thenApply(this::getOverviewFromeJson).exceptionally(ex -> Optional.empty()).get();

    }

    @Override
    public Optional<String> getTrailer(String vodId) {

        try {
            return getFutureTrailer(vodId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private  Optional<String> getLengthFromeJson(JSONObject tmdbJson){
        List<JSONObject> list = new ArrayList<JSONObject>();

        logger.debug(tmdbJson);

        if (!tmdbJson.isNull("results")){
            JSONArray results = tmdbJson.getJSONArray("results");

            for (int i=0; i<results.length(); i++) {
                JSONObject json = results.getJSONObject(i);
                list.add(json);
            }
        }

        return list.stream().
                filter(josn -> josn.getString("site").equals("YouTube")).
                sorted((f1, f2) -> Long.compare(f2.getInt("size"), f1.getInt("size"))).
                map(json -> json.getString("key")).
                findFirst();

    }

    private  Optional<String> getOverviewFromeJson(JSONObject tmdbJson){

        logger.debug(tmdbJson);

        Optional<String> overview = Optional.empty();

        if (!tmdbJson.isNull("overview")){
            overview = Optional.ofNullable(tmdbJson.getString("overview"));
        }

        return overview;
    }

}
