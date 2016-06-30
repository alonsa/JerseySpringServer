package com.alon.main.server.dao.user;

import com.alon.main.server.dao.Dao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
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

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public final class DaoMemoryUserImpl implements Dao<User> {

    private Map<Integer, User> users = new HashMap<>();

    public List<User> getByIds(List<Integer> list){
        List<User> users = new ArrayList<>();
        User user = null;
        for (Integer id: list){
            user = getById(id);
            if (user != null){
                users.add(user);
            }
        }
        return users;
    }

    public User getById(Integer id){
        return users.putIfAbsent(id, new User(id, id.toString()));
    }


}