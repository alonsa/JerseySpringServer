package com.alon.main.server.service;

import com.alon.main.server.dao.BaseDao;
import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.dao.counter.CounterMorphiaDaoImpl;
import com.alon.main.server.dao.user.UserMorphiaDaoImpl;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class UserService {

    @Autowired
    public RecommandDao<Movie> movieBaseDao;

    @Autowired
    public UserMorphiaDaoImpl userDao;

    @Autowired
    public CounterMorphiaDaoImpl counterDao;

    public User getUserByName(String userName) {
        User user = userDao.getByName(userName);
        if (user == null){
            user = new User();
            user.setName(userName);
            user.setInnerId(counterDao.increase(User.class.getSimpleName()));

            userDao.save(user);
        }
        return user;
    }


}