package com.alon.main.server.dao.user;

import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import org.springframework.stereotype.Service;

/**
 * Created by alon_ss on 6/26/16.
 */
public interface UserDao extends RecommandDao<User> {

    User getByName(String name);

}