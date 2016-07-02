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

    /**
     * Created by alon_ss on 6/26/16.
     */
    //@Service
    public static final class MovieMemoryDaoImpl {//implements Dao<Movie> {

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
}