package com.alon.main.server.movieProvider;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.http.HttpClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.*;
import static com.alon.main.server.Const.YouTubeOrderEnum.DATE;

/**
 * Created by alon_ss on 7/7/16.
 */
@Service
public class YouTubeClientServiceImpl implements YouTubeClientService {

//    https://www.googleapis.com/youtube/v3/videos?id=DhNMHcRSNdo&part=contentDetails&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA

    private final static Logger logger = Logger.getLogger(YouTubeClientServiceImpl.class);
    private final static String uriTemplate = "https://www.youtube.com/watch?v=%s";
    private static SimpleDateFormat simpleDateFormat;

    static {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//spec for RFC3339
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Autowired
    private HttpClient httpClient;

    @Value("${youtube.related.num}")
    private Integer RELATED_NUM;

    @PostConstruct
    private void init() {

        logger.debug("######################################");
        logger.debug("###   YouTubeClientService is up!  ###");
        logger.debug("######################################");
    }

    //GET https://www.googleapis.com/youtube/v3/videos?part=snippet%2C+contentDetails&id=d4HO9UY9Mb0&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA
    public Set<Movie> getVideoDetails(Set<String> videoIds){

        String strignIds = StringUtils.join(videoIds, COMMA);

        // https://www.googleapis.com/youtube/v3/videos?part=snippet%2C+contentDetails&id=d4HO9UY9Mb0&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA
        URI url = UriBuilder.
                fromUri(YOU_TUBE_BASE_URL).
                path(YOU_TUBE_VIDEOS).
                queryParam(YOU_TUBE_PART, YOU_TUBE_SNIPPET + COMMA + YOU_TUBE_CONTENT_DETAILS).
                queryParam(YOU_TUBE_ID, strignIds).
                queryParam(YOU_TUBE_KEY, YOU_TUBE_PRIVATE_KEY).
                build();

        CompletableFuture<JSONObject> data = getDataFromYoutube(url);

        Set<Movie> videos = Sets.newHashSet();

        try {
            videos.addAll(data.thenApply(this::getVideos).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return videos;
    }

    public Optional<Movie> getVideoDetails(String videoId){

        Set<String> idSet = Sets.newHashSet(videoId);
        Set<Movie> videos = getVideoDetails(idSet);

        Optional<Movie> response = Optional.empty();
        if (!videos.isEmpty()){
            response=  Optional.of(videos.iterator().next());
        }

        return response;
    }

    // https://www.googleapis.com/youtube/v3/search?channelId=UCbcovN-W9s9bkoe0wqIEjGg&part=snippet&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA&order=date&type=channel
    public Optional<ContentProvider> getContentProviderByChannelId(String channelId){

        if (channelId == null){
            return Optional.empty();
        }

        // https://www.googleapis.com/youtube/v3/search?channelId=UCbcovN-W9s9bkoe0wqIEjGg&part=snippet&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA&order=date&type=channel
        URI url = UriBuilder.
                fromUri(YOU_TUBE_BASE_URL).
                path(YOU_TUBE_SEARCH).
                queryParam(YOU_TUBE_CHANNEL_ID, channelId).
                queryParam(YOU_TUBE_PART, YOU_TUBE_SNIPPET).
                queryParam(YOU_TUBE_KEY, YOU_TUBE_PRIVATE_KEY).
                queryParam(YOU_TUBE_ORDER, DATE).
                queryParam(YOU_TUBE_TYPE, YOU_TUBE_CHANNEL).build();

        CompletableFuture<JSONObject> data = getDataFromYoutube(url);

        Optional<ContentProvider> optionalContentProvider = Optional.empty();

        try {
            optionalContentProvider = data.thenApply(this::getContentProvider).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return optionalContentProvider;
    }

    // https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCbcovN-W9s9bkoe0wqIEjGg&publishedAfter=2016-01-01T20%3A07%3A49.000Z&type=video&key=%20AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA
    public Set<String> getContentProviderVods(ContentProvider contentProvider, Long lastDataFetch){

        Date publishedAfterDate = new Date(contentProvider.getLastDataFetch());
        String publishedAfterString = simpleDateFormat.format(publishedAfterDate);

        //https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCbcovN-W9s9bkoe0wqIEjGg&publishedAfter=2016-01-01T20%3A07%3A49.000Z&type=video&key=%20AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA
        UriBuilder uriBuilder = UriBuilder.
                fromUri(YOU_TUBE_BASE_URL).
                path(YOU_TUBE_SEARCH).
                queryParam(YOU_TUBE_CHANNEL_ID, contentProvider.getYouTubeId()).
                queryParam(YOU_TUBE_PART, YOU_TUBE_SNIPPET).
                queryParam(YOU_TUBE_KEY, YOU_TUBE_PRIVATE_KEY).
                queryParam(YOU_TUBE_MAX_RESULTS, YOU_TUBE_MAX_RESULTS_VAL).
                queryParam(YOU_TUBE_ORDER, DATE).
                queryParam(YOU_TUBE_TYPE, YOU_TUBE_VIDEO).
                queryParam(YOU_TUBE_PUBLISHED_AFTER, publishedAfterString);

        List<String> contentProviderVods = Lists.newArrayList();

        Optional<String> optionalNextPageToken = addVods(uriBuilder.clone(), Optional.empty(), contentProviderVods, null);
        while (optionalNextPageToken.isPresent()){
            optionalNextPageToken = addVods(uriBuilder.clone(), optionalNextPageToken, contentProviderVods, null);
        }

        return Sets.newHashSet(contentProviderVods);
    }

    // https://www.googleapis.com/youtube/v3/search?part=snippet&relatedToVideoId=d4HO9UY9Mb0&type=video&key=AIzaSyAFk7VG3KeC4qq5Tyk1Dp4ew7UN5hnb3gA
    public List<String> getRelatedVideos(String videoId, String chanelId){
        List<String> contentProviderVods = Lists.newArrayList();

        UriBuilder uriBuilder = UriBuilder.
                fromUri(YOU_TUBE_BASE_URL).
                path(YOU_TUBE_SEARCH).
                queryParam(YOU_TUBE_PART, YOU_TUBE_SNIPPET).
                queryParam(YOU_TUBE_KEY, YOU_TUBE_PRIVATE_KEY).
                queryParam(YOU_TUBE_MAX_RESULTS, YOU_TUBE_MAX_RESULTS_VAL).
                queryParam(YOU_TUBE_ORDER, DATE).
                queryParam(YOU_TUBE_RELATED_TO_VIDEO_ID, videoId).
                queryParam(YOU_TUBE_TYPE, YOU_TUBE_VIDEO);

        Stack<String> path = new Stack<>();
        path.add(YOU_TUBE_CHANNEL_ID);
        path.add(YOU_TUBE_SNIPPET);

        HashMap<String, Stack<String>> map = Maps.newHashMap();
        map.put(chanelId, path);

        Optional<String> optionalNextPageToken = addVods(uriBuilder.clone(), Optional.empty(), contentProviderVods, map);
        while (optionalNextPageToken.isPresent() &&  RELATED_NUM > contentProviderVods.size()){
            optionalNextPageToken = addVods(uriBuilder.clone(), optionalNextPageToken, contentProviderVods, map);
        }

        return contentProviderVods;
    }

    //https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails&chart=mostpopular&key={YOUR_API_KEY}

    private Optional<String> addVods(UriBuilder uriBuilder, Optional<String> optionPageToken, List<String> vods){
        return addVods(uriBuilder, optionPageToken, vods, null);
    }

    private Optional<String> addVods(UriBuilder uriBuilder, Optional<String> optionPageToken, List<String> vods, Map<String, Stack<String>> filter){

        optionPageToken.map(pageToken -> uriBuilder.queryParam(YOU_TUBE_PAGE_TOKEN, pageToken));

        URI url = uriBuilder.build();
        Optional<String>  optionalNextPageToken = Optional.empty();
        try {

            CompletableFuture<JSONObject> data = getDataFromYoutube(url);

            optionalNextPageToken = data.
                    thenApply(json -> addVods(json, vods, filter)).
                    get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return optionalNextPageToken;
    }


    protected CompletableFuture<JSONObject> getDataFromYoutube(URI url) {
        return httpClient.
                call(url.toString()).
                thenApply(Response::getResponseBody).
                thenApply(JSONObject::new);
    }

    private Set<Movie> getVideos(JSONObject youTubeJson){
        logger.debug(youTubeJson);

        Set<Movie> movies = Sets.newHashSet();

        Optional.ofNullable(youTubeJson.optJSONArray(YOU_TUBE_ITEMS)).ifPresent(items -> {
            for (Object obj: items) {
                Movie movie = new Movie();
                JSONObject json = (JSONObject)obj;

                String id = json.optString(YOU_TUBE_ID, null);
                movie.getExternalSiteToId().put(MovieSite.YOU_TUBE, id);

                String uri = String.format(uriTemplate, id);
                try{
                    movie.setUri(URI.create(uri));
                }catch (Exception e){
                    logger.warn("Uri: " + uri + ", is a bad one");
                }

                Optional.ofNullable(json.optJSONObject(YOU_TUBE_SNIPPET)).ifPresent(snippet -> {
                    movie.setTitle(snippet.optString(YOU_TUBE_TITLE));
                    movie.setPublishDate(getPublishDate(snippet));
                    String description = snippet.optString(YOU_TUBE_DESCRIPTION);
                    if (description.isEmpty()){
                        description = movie.getTitle();
                    }
                    movie.setPlot(description);
                });

                Optional.ofNullable(json.optJSONObject(YOU_TUBE_CONTENT_DETAILS)).ifPresent(contentDetails -> {
                    String durationString = contentDetails.optString(YOU_TUBE_DURATION);
                    Optional<Long> durationOption = getDurationFromString(durationString);
                    durationOption.ifPresent(movie::setLength);

                    movie.setForbidden(contentDetails.has(YOU_TUBE_REGION_RESTRICTION));

                });
                if (isValid(movie)){
                    movies.add(movie);
                }else{
                    logger.warn("Video: " + movie + ", is not valid!");
                }
            }
        });

        return movies;
    }

    private Long getPublishDate(JSONObject snippet) {
        Long publishDate = null;
        String publishDateString = snippet.optString(YOU_TUBE_PUBLISHED_AT);
        try {

            publishDate = simpleDateFormat.parse(publishDateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return publishDate;
    }

    private Optional<Long> getDurationFromString(String durationString) {
        Optional<Long> duration = Optional.empty();
        try {
            duration = Optional.of(DatatypeFactory.newInstance().newDuration(durationString).getTimeInMillis(new Date()));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return duration;
    }


    private Optional<ContentProvider> getContentProvider(JSONObject youTubeJson){

        logger.debug(youTubeJson);

        Optional<String> nameOption = Optional.empty();
        Optional<String> idOption = Optional.empty();

        Optional<JSONArray> itemsOption = Optional.ofNullable(youTubeJson.optJSONArray(YOU_TUBE_ITEMS));

        if (itemsOption.isPresent()){
            JSONArray items = itemsOption.get();
            if (items.length() > 0) {
                JSONObject json = items.getJSONObject(0);
                Optional<JSONObject> snippetOption = Optional.ofNullable(json.optJSONObject(YOU_TUBE_SNIPPET));
                if (snippetOption.isPresent()){
                    JSONObject snippet = snippetOption.get();
                    nameOption =  Optional.ofNullable(snippet.optString(YOU_TUBE_CHANNEL_TITLE));
                    idOption =  Optional.ofNullable(snippet.optString(YOU_TUBE_CHANNEL_ID));
                }
            }
        }

        Optional<ContentProvider> optionalContentProvider = Optional.empty();

        if (nameOption.isPresent() && idOption.isPresent()){
            ContentProvider contentProvider = new ContentProvider();
            contentProvider.setName(nameOption.get());
            contentProvider.setYouTubeId(idOption.get());

            optionalContentProvider = Optional.of(contentProvider);
        }

        return optionalContentProvider;
    }

    private Optional<String> addVods(JSONObject youTubeJson, List<String> contentProviderVods, Map<String, Stack<String>> filter){

        logger.debug(youTubeJson);

        Optional<String> optionalNextPageToken = Optional.ofNullable(youTubeJson.optString(YOU_TUBE_NEXT_PAGE_TOKEN, null));

        Set<Optional<String>> optionalChannelVods = Sets.newHashSet();

        Optional.ofNullable(youTubeJson.optJSONArray(YOU_TUBE_ITEMS)).ifPresent(items -> {
            for (Object obj: items) {

                JSONObject json = (JSONObject)obj;
                if(filterJsonObject(json, filter)){
                    Optional.ofNullable(json.optJSONObject("id")).ifPresent(id -> optionalChannelVods.add(Optional.ofNullable(id.optString(YOU_TUBE_VIDEO_ID, null))));
                }
            }
        });

        Set<String> channelVods = optionalChannelVods.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        contentProviderVods.addAll(channelVods);

        return optionalNextPageToken;
    }

    public boolean filterJsonObject(JSONObject json, Map<String, Stack<String>> filter) {

        if (filter == null || filter.isEmpty()){
            return true;
        }

        boolean isValid = true;
        Iterator<String> keys = filter.keySet().iterator();


        while (isValid && keys.hasNext()){
            String key = keys.next();
            Stack<String> copiedStack = (Stack<String>)filter.get(key).clone();
            isValid = filterJsonObject(json, copiedStack, key);
        }

        return isValid;
    }

    private boolean filterJsonObject(JSONObject json, Stack<String> path, String val) {

        if (!path.isEmpty()){
            String filterValue = path.pop();
            if (json.has(filterValue)){
                if (path.isEmpty()){
                    String jsonVal = json.optString(filterValue);
                    return val.equals(jsonVal);
                }else {
                    return filterJsonObject(json.getJSONObject(filterValue), path, val);
                }
            }
        }

        return false;
    }

    private boolean isValid(Movie movie) {

        return (movie.getTitle() != null &&
                movie.getPlot() != null &&
                movie.getLength() != null &&
                movie.isForbidden() != null &&
                movie.getUri() != null &&
                movie.getExternalSiteToId().containsKey(MovieSite.YOU_TUBE));
    }

}
