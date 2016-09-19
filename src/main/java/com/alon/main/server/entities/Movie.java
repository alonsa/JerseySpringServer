package com.alon.main.server.entities;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alon.main.server.Const.MovieSite;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.scalactic.Bool;

/**
 * Created by alon_ss on 6/28/16.
 */

@Entity
public class Movie extends RecommandEntity implements Serializable{

    private String title;

    private String plot;

    private URI uri;

    private List<String> genres = new ArrayList<>();

    private List<ExternalId> externalIds = new ArrayList<>();

    private Map<MovieSite, String> externalSiteToId = new HashMap<>();

    private Long length;

    private Long publishDate;

    private Boolean forbidden;

    private List<ObjectId> relatedVods = new ArrayList<>();

    public Movie(){}

    public Movie(Integer id, String title, List<String> genres, List<ExternalId> externalIds) {

        this.setInnerId(id);
        this.title = title;
        this.externalIds = externalIds;
        this.genres = genres;
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

    @Deprecated
    public List<ExternalId> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(List<ExternalId> externalIds) {
        this.externalIds = externalIds;
    }

    public Map<MovieSite, String> getExternalSiteToId() {
        return externalSiteToId;
    }

    public void setExternalSiteToId(Map<MovieSite, String> externalSiteToId) {
        this.externalSiteToId = externalSiteToId;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI trailer) {
        this.uri = trailer;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Boolean isForbidden() {
        return forbidden;
    }

    public void setForbidden(Boolean forbidden) {
        this.forbidden = forbidden;
    }

    public Boolean getForbidden() {
        return forbidden;
    }

    public List<ObjectId> getRelatedVods() {
        return relatedVods;
    }

    public void setRelatedVods(List<ObjectId> relatedVods) {
        this.relatedVods = relatedVods;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Long publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "innerId=" + this.getInnerId() +
                ", title='" + title + '\'' +
                ", plot='" + plot + '\'' +
                ", uri=" + uri +
                ", genres=" + genres +
                ", externalIds=" + externalIds +
                '}';
    }
}


