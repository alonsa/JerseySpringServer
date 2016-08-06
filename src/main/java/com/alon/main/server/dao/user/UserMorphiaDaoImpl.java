package com.alon.main.server.dao.user;

import com.alon.main.server.dao.MorphiaRecommandDao;
import com.alon.main.server.entities.User;
import org.mongodb.morphia.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import static com.alon.main.server.Const.Consts.NAME_FIELD;
import static com.alon.main.server.Const.Consts.USER_DB;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public class UserMorphiaDaoImpl extends MorphiaRecommandDao<User> implements UserDao{

    @Override
    public Class<User> getTypeClass() {
        return User.class;
    }

    @Override
    protected String getDbName() {
        return USER_DB;
    }

    public User getByName(String name) {
        Query<User> query = getQuery();
        query.field(NAME_FIELD).equal(name);
        return query.get();
    }


}