package com.alon.main.server.rest;

import com.alon.main.server.entities.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alon_ss on 7/6/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EPG {

    private String id;

    private String title;

    private String plot;

    private String uri;

    private List<String> genres = new ArrayList<>();

    EPG() {
    }

    EPG(Movie movie) {
        this.id = movie.getId().toString();
        this.title = movie.getTitle();
        this.plot = movie.getPlot();
        this.uri = movie.getUri().toString();
        this.genres = movie.getGenres();
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
}
