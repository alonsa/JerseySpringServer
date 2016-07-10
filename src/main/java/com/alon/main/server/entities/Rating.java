package com.alon.main.server.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by alon_ss on 6/28/16.
 */

@Entity
@XmlRootElement
public class Rating extends BaseEntity implements Serializable{

    private Integer userId;

    private Integer movieId;

    private Double rating;

    private Long timestamp;


    public Rating(){};

    public Rating(Integer userId, Integer movieId, Double rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = System.currentTimeMillis();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}


