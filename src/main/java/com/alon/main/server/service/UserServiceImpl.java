package com.alon.main.server.service;

import com.alon.main.server.dao.counter.CounterDao;
import com.alon.main.server.dao.counter.CounterMorphiaDaoImpl;
import com.alon.main.server.dao.user.UserDao;
import com.alon.main.server.dao.user.UserMorphiaDaoImpl;
import com.alon.main.server.entities.BaseEntity;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class UserServiceImpl extends RecommendEntityServiceImpl<User> implements UserService{

    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private ContentProviderService contentProviderService;

    @Autowired
    public UserServiceImpl(UserDao userDao, ContentProviderService contentProviderService) {
        this.userDao = userDao;
        this.contentProviderService = contentProviderService;

        logger.debug("##############################");
        logger.debug("###   UserService is up!   ###");
        logger.debug("##############################");
    }

    public User getUserByName(String userName) {
        User user = userDao.getByName(userName);
        if (user == null){
            user = getAndSaveUserByName(userName);
        }
        return user;
    }

    private User getAndSaveUserByName(String userName){
        User user = new User();
        user.setName(userName);

        List<ContentProvider> contentProviderList = contentProviderService.getAll();
        ObjectId contentProviderId = contentProviderList.stream()
                .filter(cp -> userName.contains(cp.getName()))
                .map(BaseEntity::getId)
                .findFirst()
                .orElse(new ObjectId("57d11fd065ff5b8a81c97685"));

        user.setCpId(contentProviderId);

        save(user);

        return user;
    }

}