package com.alon.main.server.dao;

import com.alon.main.server.entities.ContentProviderEntity;
import com.alon.main.server.entities.RecommandEntity;
import com.alon.main.server.entities.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.ValidationException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.CP_ID_FIELD;
import static com.alon.main.server.Const.Consts.ID_FIELD;
import static com.alon.main.server.Const.Consts.INNER_ID_FIELD;

/**
 * Created by alon_ss on 6/29/16.
 */

public abstract class MorphiaContentProviderEntityDao<T extends ContentProviderEntity> extends MorphiaBaseDao<T>  {

    // sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db

    private Query<T> addCpFilter(Query<T> query, ObjectId cpId) {
        return query.filter(CP_ID_FIELD, cpId);
    }

    @Override
    protected Query<T> getQuery(){
        Query<T> query = super.getQuery();

        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null){
            User user = (User) context.getAuthentication().getPrincipal();
            if (user.getCpId() != null){
                addCpFilter(query, user.getCpId());
            }
        }

        return query;
    }

    @Override
    public Key<T> save(T entity) {

        if (entity.getCpId() == null){
            throw new ValidationException("Entity:" + entity + ", doesn't have CpId");
        }

        return super.save(entity);
    }


}


