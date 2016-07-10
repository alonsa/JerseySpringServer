package com.alon.main.server.entities;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import javax.xml.bind.annotation.XmlRootElement;

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

    private Long length;

    public Movie(){};

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

    public List<ExternalId> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(List<ExternalId> externalIds) {
        this.externalIds = externalIds;
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


