package com.alon.main.server.service;

import com.alon.main.server.dao.counter.CounterDao;
import com.alon.main.server.dao.user.UserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class CounterServiceImpl implements CounterService{

    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private CounterDao counterDao;

    @Autowired
    public CounterServiceImpl(CounterDao counterDao) {
        this.counterDao = counterDao;

        logger.debug("##############################");
        logger.debug("###   UserService is up!   ###");
        logger.debug("##############################");
    }

    @Override
    public Integer increaseEntityCounter(String entityName) {
        return counterDao.increase(entityName);
    }
}