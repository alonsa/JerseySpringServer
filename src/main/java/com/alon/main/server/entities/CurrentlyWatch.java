package com.alon.main.server.entities;

import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by alon_ss on 7/10/16.
 */
public class CurrentlyWatch {
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
