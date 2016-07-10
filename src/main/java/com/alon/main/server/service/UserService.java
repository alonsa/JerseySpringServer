package com.alon.main.server.service;

import com.alon.main.server.dao.BaseDao;
import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.dao.counter.CounterMorphiaDaoImpl;
import com.alon.main.server.dao.user.UserMorphiaDaoImpl;
import com.alon.main.server.entities.CurrentlyWatch;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.Rating;
import com.alon.main.server.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class UserService {

    @Autowired
    public UserMorphiaDaoImpl userDao;

    @Autowired
    public CounterMorphiaDaoImpl counterDao; // TODO change to Service

    @Autowired
    public RatingService ratingService; // TODO change to Service

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


    public void save(User user) {
        userDao.save(user);
    }

}