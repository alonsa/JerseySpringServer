package com.alon.main.server.dbMigrtor;

import com.alon.main.server.dao.BaseDao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.MovieProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by alon_ss on 6/26/16.
 */
//@Service //- Just to save all movies in DB
public final class DbMigrator {

    private final static Logger logger = Logger.getLogger(DbMigrator.class);

    @Autowired
	public MovieMemoryDaoImpl movieMemoryDaoImpl;

    @Autowired
    public BaseDao<Movie> movieBaseDao;

    @PostConstruct
    protected void init() {
//        saveMovies(null);
        addUrlToMovies();
//        addDefaultUrlToMovies();

    }

    private void addUrlToMovies() {
        // take movies from DB and add data from TMDB
        Integer numToSkip = 4600;
        Iterator<Movie> moviesIter = movieBaseDao.getNoUrl(numToSkip);

        MovieProvider movieProvider = new MovieProvider();

        int i = numToSkip;
        int updated = 0;

        while (moviesIter.hasNext()){
            Movie movie = moviesIter.next();

            if (movie.getUri() == null){

                logger.debug(i);
                logger.debug(movie);

                String trailer = movieProvider.getYouTubeTrailer(movie);

                if (trailer != null){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uri", trailer);

                    logger.debug("UPDATE trailer" + trailer);

                    movieBaseDao.updateByField(movie, map);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                if ((updated % 20) == 0){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                updated ++;
            }

            i++;
        }
    }

    private void addDefaultUrlToMovies() {
        // take movies from DB and add data from TMDB
        Iterator<Movie> moviesIter = movieBaseDao.getAll();

        List<String> movieProvider = new ArrayList<>();

        movieProvider.add("https://www.youtube.com/watch?v=LTgRm6Qgscc");
        movieProvider.add("https://www.youtube.com/watch?v=DhNMHcRSNdo");
        movieProvider.add("https://www.youtube.com/watch?v=hvha-7EvwNg");
        movieProvider.add("https://www.youtube.com/watch?v=kvg9GxWjgIw");
        movieProvider.add("https://www.youtube.com/watch?v=OT9HsNszYCI");

        int i = 0;

        while (moviesIter.hasNext()){
            Movie movie = moviesIter.next();

            if (movie.getUri() == null){

                logger.debug(i);
                logger.debug(movie);

                String trailer =  movieProvider.get(i % movieProvider.size());

                if (trailer != null){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uri", trailer);

                    logger.debug("UPDATE trailer" + trailer);

                    movieBaseDao.updateByField(movie, map);

//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            i++;
        }
    }

    private void saveMovies(Integer num) {
        //         take movies from xml file and save them in DB
        List<Movie> list = movieMemoryDaoImpl.getAll(num);
        movieBaseDao.saveAll(list);
        list.toString();
    }

}