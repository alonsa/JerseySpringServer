package com.alon.main.server.dbMigrtor;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.service.RecommenderService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.alon.main.server.Const.Consts.COMMA;
import static com.alon.main.server.Const.Consts.MOVIES_PATH;
import static com.alon.main.server.Const.Consts.VERTICAL_BAR;

/**
 * Created by alon_ss on 6/26/16.
 */
//@Service
public final class MovieMemoryDaoImpl {//implements Dao<Movie> {

    @Autowired
	public RecommenderService recommenderService;

    private JavaSparkContext sc;
    private JavaRDD<Movie> moviesRdd;

    @PostConstruct
    @Async
    private void init() {
        sc = recommenderService.getJavaSparkContext();
        loadMovies();
    }

    public List<Movie> getAll(){
        return moviesRdd.collect();
    }



    private void loadMovies() {
        JavaRDD<String> movieLines = sc.textFile(MOVIES_PATH);

        String firstMovieLine = movieLines.first();

        moviesRdd = movieLines.
                filter(line -> !line.equals(firstMovieLine)).
                map(MovieMemoryDaoImpl::parseMovie);
    }

    private static Movie parseMovie(String str) {
        String[] tok = COMMA.split(str);

        int id = Integer.parseInt(tok[0]);
        String title = tok[1];
        List<String> genres = Arrays.asList(VERTICAL_BAR.split(tok[2]));

        List<ExternalId> externalIds = new ArrayList<>();

        externalIds.add(new ExternalId(MovieSite.IMDB, tok[3]));

        Optional<String> trailer = Optional.empty();

        if (tok.length >=5){
            externalIds.add(new ExternalId(MovieSite.TMDB, tok[4]));
        }

        return new Movie(id, title, genres, externalIds);
    }

}