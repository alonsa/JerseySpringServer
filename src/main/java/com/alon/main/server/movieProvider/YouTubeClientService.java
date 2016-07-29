package com.alon.main.server.movieProvider;

import com.alon.main.server.http.HttpClient;
import org.apache.log4j.Logger;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.net.URI;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 7/7/16.
 */
@Service
public class YouTubeClientService {

//    https://www.googleapis.com/youtube/v3/videos?id=DhNMHcRSNdo&part=contentDetails&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA

    private final static Logger logger = Logger.getLogger(YouTubeClientService.class);

    public Optional<Long> getVodLength(String vodId) {

        if (vodId == null){
            return Optional.empty();
        }
        //https://www.googleapis.com/youtube/v3/videos?id=DhNMHcRSNdo&part=contentDetails&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA
        URI url = UriBuilder.
                fromUri(YOU_TUBE_BASE_URL).
                path(YOU_TUBE_VIDEOS).
                queryParam(YOU_TUBE_ID, vodId).
                queryParam(YOU_TUBE_PART, YOU_TUBE_CONTENT_DETAILS).
                queryParam(YOU_TUBE_KEY, YOU_TUBE_PRIVATE_KEY).
                build();

        Optional<Long> durationOptional = Optional.empty();
        try {

            CompletableFuture<JSONObject> data = getDataFromYoutube(url);

            durationOptional = data.
                    thenApply(this::getDuration).
                    get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return durationOptional;

    }

    protected CompletableFuture<JSONObject> getDataFromYoutube(URI url) {
        return HttpClient.
                call(url.toString()).
                thenApply(Response::getResponseBody).
                thenApply(JSONObject::new);
    }


    private  Optional<Long> getDuration(JSONObject youTubeJson){
        Optional<Long> duration = Optional.empty();
        logger.debug(youTubeJson);

        if (!youTubeJson.isNull("items")){
            JSONArray results = youTubeJson.getJSONArray("items");

            if (results.length() > 0){
                JSONObject json = results.getJSONObject(0);

                JSONObject contentDetails = json.getJSONObject("contentDetails");
                if (contentDetails.isNull("regionRestriction")){
                    String durationString = contentDetails.getString("duration");
                    try {
                        duration = Optional.of(DatatypeFactory.newInstance().newDuration(durationString).getTimeInMillis(new Date()));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return duration;
    }


}
