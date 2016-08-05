package com.alon.main.server.entities;

import org.bson.types.ObjectId;
import org.spark_project.jetty.util.ArrayQueue;

import java.io.Serializable;
import java.util.*;

/**
 * Created by alon_ss on 6/28/16.
 */
public class User extends RecommandEntity implements Serializable{

    private String name;

    private List<ObjectId> recentlyWatch = new ArrayList<ObjectId>();

    private CurrentlyWatch currentlyWatch;

    private List<ObjectId> nextVods  = new ArrayList<>();

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
            addToRecentlyWatch(currentlyWatch.getMovieId());
        }
    }

    public void removeFromRecentlyWatch(ObjectId movieId) {
        recentlyWatch.remove(movieId);
    }

    public List<ObjectId> pullFromNextVodList(Integer num) {
        List<ObjectId> nextVodList = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            if (!nextVods.isEmpty()){
                nextVodList.add(nextVods.remove(0));
            }
        }

        return nextVodList;
    }

    public void addToHeadNextVodList(ObjectId id) {
        nextVods.add(0, id);
    }

    public void addToNextVodList(List<ObjectId> ids) {
        nextVods.addAll(ids);
    }

    public List<ObjectId> getNextVods() {
        return nextVods;
    }

    public void setNextVods(List<ObjectId> nextVods) {
        this.nextVods = nextVods;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", recentlyWatch=" + recentlyWatch +
                ", currentlyWatch=" + currentlyWatch +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
        if (getRecentlyWatch() != null ? !getRecentlyWatch().equals(user.getRecentlyWatch()) : user.getRecentlyWatch() != null)
            return false;
        if (getCurrentlyWatch() != null ? !getCurrentlyWatch().equals(user.getCurrentlyWatch()) : user.getCurrentlyWatch() != null)
            return false;
        return getNextVods() != null ? getNextVods().equals(user.getNextVods()) : user.getNextVods() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getRecentlyWatch() != null ? getRecentlyWatch().hashCode() : 0);
        result = 31 * result + (getCurrentlyWatch() != null ? getCurrentlyWatch().hashCode() : 0);
        result = 31 * result + (getNextVods() != null ? getNextVods().hashCode() : 0);
        return result;
    }
}
