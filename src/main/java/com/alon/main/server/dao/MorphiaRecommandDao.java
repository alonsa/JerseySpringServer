package com.alon.main.server.dao;

import com.alon.main.server.entities.RecommandEntity;
import org.mongodb.morphia.query.Query;

import java.util.List;

import static com.alon.main.server.Const.Consts.INNER_ID_FIELD;

/**
 * Created by alon_ss on 6/29/16.
 */

public abstract class MorphiaRecommandDao<T extends RecommandEntity> extends MorphiaBaseDao<T> implements RecommandDao<T> {

    // sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db

    @Override
    public List<T> getByInnerIds(List<Integer> list) {
        Query<T> query = getQuery();
        query.field(INNER_ID_FIELD).in(list);
        return query.asList();
    }

    @Override
    public T getByInnerId(Integer id) {
        Query<T> query = getQuery();
        query.field(INNER_ID_FIELD).equal(id);
        return query.get();
    }

    @Override
    public Integer getMaxInnerId(){
        Query<T> query = getQuery();

        Object entity = query.order(INNER_ID_FIELD).field(INNER_ID_FIELD);
        if (entity == null){
            return 0;
        }else{
            //noinspection ConstantConditions
            return (Integer) entity;
        }
    }




}


