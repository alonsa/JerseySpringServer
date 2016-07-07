package com.alon.main.server.entities;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.alon.main.server.Const.Consts.RECENTLY_WATCH_MAX_SIZE;

/**
 * Created by alon_ss on 6/28/16.
 */
public class User extends RecommandEntity implements Serializable{

    private String name;

    private CircularFifoQueue<ObjectId> recentlyWatch = new CircularFifoQueue<ObjectId>(RECENTLY_WATCH_MAX_SIZE);

    public User() {}

    public User(Integer innerId, String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CircularFifoQueue<ObjectId> getRecentlyWatch() {
        return recentlyWatch;
    }

    public void addToRecentlyWatch(ObjectId movieId) {
        recentlyWatch.add(movieId);
    }

    public void removeFromRecentlyWatch(ObjectId movieId) {
        recentlyWatch.remove(movieId);
    }



}
