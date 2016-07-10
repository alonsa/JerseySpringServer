package com.alon.main.server.entities;

import com.alon.main.server.Const.MovieSite;
import org.mongodb.morphia.annotations.Embedded;

import java.io.Serializable;

/**
 * Created by alon_ss on 6/28/16.
 */
@Embedded
public class ExternalId implements Serializable{
    private MovieSite siteName;
    private String id;

    public ExternalId() {}

    public ExternalId(MovieSite siteName, String id) {
        this.siteName = siteName;
        this.id = id;
    }

    public MovieSite getSiteName() {
        return siteName;
    }

    public void setSiteName(MovieSite siteName) {
        this.siteName = siteName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
