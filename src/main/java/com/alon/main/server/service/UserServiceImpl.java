package com.alon.main.server.service;

import com.alon.main.server.dao.counter.CounterDao;
import com.alon.main.server.dao.counter.CounterMorphiaDaoImpl;
import com.alon.main.server.dao.user.UserDao;
import com.alon.main.server.dao.user.UserMorphiaDaoImpl;
import com.alon.main.server.entities.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class UserServiceImpl implements UserService{

    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private UserDao userDao;

    private CounterDao counterDao; // TODO change to Service

    @Autowired
    public UserServiceImpl(CounterDao counterDao, UserDao userDao) {
        this.counterDao = counterDao;
        this.userDao = userDao;

        logger.debug("##############################");
        logger.debug("###   UserService is up!   ###");
        logger.debug("##############################");
    }

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