package com.alon.main.server.service;

import com.alon.main.server.entities.User;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface UserService extends RecommendEntityService<User> {

    User getUserByName(String userName);

}