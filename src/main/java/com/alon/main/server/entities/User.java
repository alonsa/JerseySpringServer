package com.alon.main.server.entities;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.alon.main.server.Const.Consts.RECENTLY_WATCH_MAX_SIZE;

/**
 * Created by alon_ss on 6/28/16.
 */
public class User implements Serializable{
    private Integer id;
    private String name;
    private CircularFifoQueue<Integer> recentlyWatch = new CircularFifoQueue<Integer>(RECENTLY_WATCH_MAX_SIZE);

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
