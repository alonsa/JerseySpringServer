package com.alon.main.server.dao.movie;

import com.alon.main.server.dao.Dao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.MovieProvider;
import com.alon.main.server.service.RecommenderService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.COMMA;
import static com.alon.main.server.Const.Consts.MOVIES_PATH;
import static com.alon.main.server.Const.Consts.VERTICAL_BAR;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public final class DaoMemoryMovieImpl implements Dao<Movie> {

    @Autowired
	public RecommenderService recommenderService;

    private JavaSparkContext sc;
    private JavaRDD<Movie> moviesRdd;

    private static MovieProvider movieProvider = new MovieProvider();

    @Override
    public List<Movie> getByIds(List<Integer> list){
        Set<Integer> set = new HashSet<>(list);
        List<Movie> movies = moviesRdd.filter(movie -> set.contains(movie.getId())).
                map(x -> Tuple2.apply(x, movieProvider.getYouTubeFutureTrailer(x))).
                map(DaoMemoryMovieImpl::executeHttpCall).map(DaoMemoryMovieImpl::updateMovie).
                collect();
        return movies;
    }

    @Override
    public Movie getById(Integer id) {
        List<Integer> list = new ArrayList<>(1);
        List<Movie> responseList = getByIds(list);
        if (responseList.isEmpty()) {
            return null;
        } else {
            return responseList.get(0);
        }
    }

    @PostConstruct
    @Async
    private void init() {
        sc = recommenderService.getJavaSparkContext();
        loadMovies();
    }

    private static Tuple2<Movie, URI> executeHttpCall(Tuple2<Movie, CompletableFuture<Optional<URI>>> tuple) {
        URI str = null;
        try {
            str = tuple._2().get().orElse(null);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tuple._1.setTrailer(str);

        return Tuple2.apply(tuple._1, str);
    }

    private static Movie updateMovie(Tuple2<Movie, URI> tuple){
        tuple._1.setTrailer(tuple._2);
        return tuple._1;
    }

    private void loadMovies() {
        JavaRDD<String> movieLines = sc.textFile(MOVIES_PATH);

        String firstMovieLine = movieLines.first();

        moviesRdd = movieLines.
                filter(line -> !line.equals(firstMovieLine)).
                map(DaoMemoryMovieImpl::parseMovie);
    }

    private static Movie parseMovie(String str) {
        String[] tok = COMMA.split(str);

        int id = Integer.parseInt(tok[0]);
        String title = tok[1];
        List<String> genres = Arrays.asList(VERTICAL_BAR.split(tok[2]));

        String imdbId = tok[3];
        String tmdbId = null;
        Optional<String> trailer = Optional.empty();

        if (tok.length >=5){
            tmdbId = tok[4];
        }

        return new Movie(id, title, imdbId, tmdbId, genres);
    }

}