package com.alon.main.server.rest;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.CharMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private String youTubeId;

    private List<String> genres = new ArrayList<>();

    Epg(Movie movie) {
        this.id = movie.getId().toString();
        this.plot = movie.getPlot();

        this.genres = movie.getGenres();

        this.title = Optional.ofNullable(movie.getTitle()).map(CharMatcher.ASCII::retainFrom).orElse(null);
        this.uri = movie.getUri().toString();
        this.youTubeId = movie.getExternalSiteToId().getOrDefault(MovieSite.YOU_TUBE, "");
        this.length = movie.getLength();
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

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
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
                ", youTubeId=" + youTubeId +
                '}';
    }


}