package com.alon.main.server.entities;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;

import java.io.Serializable;

import static com.alon.main.server.Const.Consts.RECENTLY_WATCH_MAX_SIZE;

/**
 * Created by alon_ss on 6/28/16.
 */
@Embedded
public class ExternalId implements Serializable{
    private String siteName;
    private String id;

    public ExternalId() {}

    public ExternalId(String siteName, String id) {
        this.siteName = siteName;
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
