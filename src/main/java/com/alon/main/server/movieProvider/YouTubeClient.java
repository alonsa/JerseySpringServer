package com.alon.main.server.movieProvider;

import com.alon.main.server.http.HttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.net.URI;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 7/7/16.
 */
public class YouTubeClient {


//    https://www.googleapis.com/youtube/v3/videos?id=DhNMHcRSNdo&part=contentDetails&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA


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
            durationOptional = HttpClient.
                    call(url.toString()).
                    thenApply(Response::getResponseBody).
                    thenApply(JSONObject::new).
                    thenApply(this::getDuration).
                    exceptionally(ex -> Optional.empty()).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return durationOptional;

    }


    private  Optional<Long> getDuration(JSONObject youTubeJson){
        Optional<Long> duration = Optional.empty();
        System.out.println(youTubeJson);

        if (!youTubeJson.isNull("items")){
            JSONArray results = youTubeJson.getJSONArray("items");

            if (results.length() > 0){
                JSONObject json = results.getJSONObject(0);
                String durationString = json.getJSONObject("contentDetails").getString("duration");
                try {
                    duration = Optional.of(DatatypeFactory.newInstance().newDuration(durationString).getTimeInMillis(new Date()));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }

        return duration;
    }


}
