package com.alon.main.server.dao;

import com.alon.main.server.entities.RecommandEntity;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.INNER_ID_FIELD;

/**
 * Created by alon_ss on 6/29/16.
 */

public abstract class MorphiaRecommandDao<T extends RecommandEntity> extends MorphiaContentProviderEntityDao<T> implements RecommandDao<T> {

    // sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db

    @Override
    public List<T> getByInnerIds(List<Integer> list) {
        Query<T> query = getQuery();
        query.field(INNER_ID_FIELD).in(list);
        return getQueryList(query);
    }

    @Override
    public List<T> getOrderedByInnerIds(List<Integer> list) {
        List<T> entities = getByInnerIds(list);
        Map<Integer, T> idToEntity = entities.stream().collect(Collectors.toMap(T::getInnerId, Function.identity()));
        return list.stream().map(idToEntity::get).collect(Collectors.toList());
    }

    @Override
    public T getByInnerId(Integer id) {
        Query<T> query = getQuery();
        query.field(INNER_ID_FIELD).equal(id);
        return getQueryEntity(query);
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


