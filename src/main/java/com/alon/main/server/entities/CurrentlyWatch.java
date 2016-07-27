package com.alon.main.server.entities;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by alon_ss on 7/10/16.
 */
public class CurrentlyWatch {

    @Embedded
    private Movie movie;
    private Long startWatchTime;


    public CurrentlyWatch() {
    }

    public CurrentlyWatch(Movie movie) {
        this.movie = movie;

        this.startWatchTime = System.currentTimeMillis();
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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
                "movie=" + movie +
                ", startWatchTime=" + startWatchTime +
                '}';
    }
}
