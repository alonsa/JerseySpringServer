package com.alon.main.server.entities;

import org.bson.types.ObjectId;

/**
 * Created by alon_ss on 7/10/16.
 */
public class CurrentlyWatch {

    private ObjectId movieId;
    private Long startWatchTime;

    public CurrentlyWatch() {
    }

    public CurrentlyWatch(ObjectId movieId) {
        this.movieId = movieId;

        this.startWatchTime = System.currentTimeMillis();
    }

    public ObjectId getMovieId() {
        return movieId;
    }

    public void setMovie(ObjectId movieId) {
        this.movieId = movieId;
    }

    public Long getStartWatchTime() {
        return startWatchTime;
    }

    public void setStartWatchTime(Long startWatchTime) {
        this.startWatchTime = startWatchTime;
    }

    @Override
    public String toString() {
        return "CurrentlyWatch{" +
                "movie=" + movieId +
                ", startWatchTime=" + startWatchTime +
                '}';
    }
}
