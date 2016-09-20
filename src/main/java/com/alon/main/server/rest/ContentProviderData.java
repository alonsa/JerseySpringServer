package com.alon.main.server.rest;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.CharMatcher;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by alon_ss on 7/6/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContentProviderData {

    private String youTubeId;

    private String name;

    private Boolean isNew;

    private Integer vodNumber;

    public ContentProviderData(String youTubeId, String name, Boolean isNew, int vodNumber) {
        this.youTubeId = youTubeId;
        this.name = name;
        this.isNew = isNew;
        this.vodNumber = vodNumber;
    }

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Integer getVodNumber() {
        return vodNumber;
    }

    public void setVodNumber(Integer vodNumber) {
        this.vodNumber = vodNumber;
    }
}