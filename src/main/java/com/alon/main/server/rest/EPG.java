package com.alon.main.server.rest;

import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.YouTubeClient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.CharMatcher;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by alon_ss on 7/6/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Epg {

    private String id;

    private String title;

    private Long length;

    private String plot;

    private String uri;

    private List<String> genres = new ArrayList<>();

    Epg() {
    }

    Epg(Movie movie) {
        this.id = movie.getId().toString();
        this.plot = movie.getPlot();

        this.genres = movie.getGenres();

        this.title = Optional.ofNullable(movie.getTitle()).map(CharMatcher.ASCII::retainFrom).orElse(null);
        this.uri = Optional.ofNullable(convertToEmbedUri(movie.getUri())).orElse(getDefualtUri()).toString();



        setLengthFromYouTube();


        long sec = length / 1000;
        double minutes = Math.floor(sec / 60);
        Double seconds = Math.floor(sec % 60);
        seconds.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Epg{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", length=" + length +
                ", plot='" + plot + '\'' +
                ", uri='" + uri + '\'' +
                ", genres=" + genres +
                '}';
    }

    @Deprecated
    private URI convertToEmbedUri(URI uri){
        URI newUri = null;
        if (uri != null){
            newUri = UriBuilder.
                    fromPath(uri.getHost()).
                    path("embed").
                    path(getYouTubeId(uri)).
                    queryParam("autoplay", 1).
                    build();

        }
        return newUri;
    }

    @Deprecated
    private String getYouTubeId(URI uri){
        String youTubeId = null;
        if (uri != null){
            youTubeId = uri.getQuery().replace("v=", "");
        }
        return youTubeId;
    }

    private void setLengthFromYouTube() {
        try {
            String youTubeId = (new  URI(this.uri)).getPath().
                    replace("https://", "").
                    replace("www.youtube.com", "").
                    replace("embed", "").
                    replace("/", "");

            YouTubeClient youTubeClient = new YouTubeClient();
            Optional<Long> optionalVodLength = youTubeClient.getVodLength(youTubeId);
            this.length = optionalVodLength.orElse(null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    private URI getDefualtUri() {

        List<URI> uris = new ArrayList<>();
        try {
            uris.add(new URI("https://www.youtube.com/embed/LTgRm6Qgscc?autoplay=1"));
            uris.add(new URI("https://www.youtube.com/embed/DhNMHcRSNdo?autoplay=1"));
            uris.add(new URI("https://www.youtube.com/embed/hvha-7EvwNg?autoplay=1"));
            uris.add(new URI("https://www.youtube.com/embed/kvg9GxWjgIw?autoplay=1"));
            uris.add(new URI("https://www.youtube.com/embed/OT9HsNszYCI?autoplay=1"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        int index = (Math.abs(id.hashCode()) % uris.size());

        return uris.get(index);
    }

}