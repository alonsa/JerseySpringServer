package com.alon.main.server.entities;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.*;

/**
 * Created by alon_ss on 6/28/16.
 */
public class User extends RecommandEntity implements Serializable{

    private String name;

    private List<ObjectId> recentlyWatch = new ArrayList<ObjectId>();

    private CurrentlyWatch currentlyWatch;

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

    public List<ObjectId> getRecentlyWatch() {
        return recentlyWatch;
    }

    public void addToRecentlyWatch(ObjectId movieId) {
        recentlyWatch.add(movieId);
    }

    public void addToRecentlyWatch(List<ObjectId> movieIds) {
        recentlyWatch.addAll(movieIds);
    }

    public void setRecentlyWatch(List<ObjectId> recentlyWatch) {
        this.recentlyWatch = recentlyWatch;
    }

    public CurrentlyWatch getCurrentlyWatch() {
        return currentlyWatch;
    }

    public void setCurrentlyWatch(CurrentlyWatch currentlyWatch) {
        if (currentlyWatch != null){
            this.currentlyWatch = currentlyWatch;
            addToRecentlyWatch(currentlyWatch.getMovie().getId());
        }
    }

    public void removeFromRecentlyWatch(ObjectId movieId) {
        recentlyWatch.remove(movieId);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", recentlyWatch=" + recentlyWatch +
                ", currentlyWatch=" + currentlyWatch +
                '}';
    }
}
