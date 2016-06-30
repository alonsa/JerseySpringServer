package com.alon.main.server.entities;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alon_ss on 6/28/16.
 */
public class Movie implements Serializable{
    private Integer id;
    private String title;
    private String plot;
    private String imdbId;
    private String tmdbId;
    private URI trailer;
    private List<String> genres = new ArrayList<>();

    public Movie(Integer id, String title, String imdbId, String tmdbId, List<String> genres) {
        this.id = id;
        this.title = title;
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
        this.genres = genres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public URI getTrailer() {
        return trailer;
    }

    public void setTrailer(URI trailer) {
        this.trailer = trailer;
    }
}
