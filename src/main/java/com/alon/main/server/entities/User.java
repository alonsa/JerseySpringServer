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
public class User implements Serializable{
    private Integer innerId;
    private ObjectId id;
    private String name;
    private CircularFifoQueue<Integer> recentlyWatch = new CircularFifoQueue<Integer>(RECENTLY_WATCH_MAX_SIZE);

    public User() {}

    public User(ObjectId id, Integer innerId, String name) {
        this.id = id;
        this.name = name;
        this.innerId = innerId;
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

    public Integer getInnerId() {
        return innerId;
    }

    public void setInnerId(Integer innerId) {
        this.innerId = innerId;
    }

    public CircularFifoQueue<Integer> getRecentlyWatch() {
        return recentlyWatch;
    }

    public void addToRecentlyWatch(Integer movieId) {
        recentlyWatch.add(movieId);
    }

    public void removeFromRecentlyWatch(Integer movieId) {
        recentlyWatch.remove(movieId);
    }



}
