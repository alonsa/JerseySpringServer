package com.alon.main.server.rest;

import com.alon.main.server.entities.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by alon_ss on 7/6/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EPG {

    private String id;

    private String title;

    private String plot;

    private String uri;

    private List<String> genres = new ArrayList<>();

    EPG() {
    }

    EPG(Movie movie) {
        this.id = movie.getId().toString();
        this.title = movie.getTitle();
        this.plot = movie.getPlot();

        this.genres = movie.getGenres();

        this.uri = Optional.ofNullable(movie.getUri()).orElse(getDefualtUri()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @Deprecated
    private URI getDefualtUri() {
        Random randomGenerator = new Random();

        List<URI> uris = new ArrayList<>();
        try {
            uris.add(new URI("https://www.youtube.com/watch?v=LTgRm6Qgscc"));
            uris.add(new URI("https://www.youtube.com/watch?v=DhNMHcRSNdo"));
            uris.add(new URI("https://www.youtube.com/watch?v=hvha-7EvwNg"));
            uris.add(new URI("https://www.youtube.com/watch?v=kvg9GxWjgIw"));
            uris.add(new URI("https://www.youtube.com/watch?v=OT9HsNszYCI"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        int index = randomGenerator.nextInt(uris.size());


        return uris.get(index);
    }



    //

}
