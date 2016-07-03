package com.alon.main.server.dbMigrtor;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.dao.Dao;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.service.RecommenderService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.alon.main.server.Const.Consts.COMMA;
import static com.alon.main.server.Const.Consts.MOVIES_PATH;
import static com.alon.main.server.Const.Consts.VERTICAL_BAR;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service //- Just to save all movies in DB
public final class DbMigrator {

//    @Autowired
//	public MovieMemoryDaoImpl movieMemoryDaoImpl;

    @Autowired
    public Dao<Movie> movieDao;

    @PostConstruct
    protected void init() {

        // take movies from xml file and save them in DB
//        List<Movie> list = movieMemoryDaoImpl.getAll();
//        movieDao.saveAll(list);
//        list.toString();

        // take movies from DB and add data from TMDB
        Iterator<Movie> moviesIter = movieDao.getAll();

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

                System.out.println(i);
                System.out.println(movie);

                String trailer =  movieProvider.get(i % movieProvider.size());

                if (trailer != null){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uri", trailer);

                    System.out.println("UPDATE trailer" + trailer);

                    movieDao.updateByField(movie, map);

//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }




            i++;
        }

        String d  = "d";


    }

}