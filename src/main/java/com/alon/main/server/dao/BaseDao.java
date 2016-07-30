package com.alon.main.server.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alon_ss on 6/29/16.
 */
public interface BaseDao<T> {

    List<T> getByIds(List<ObjectId> list);

    Long count();

    T getById(ObjectId id);

    @Deprecated
    Iterator<T> getNoUrl(Integer skip);

    Key<T> save(T entity);
    Iterable<Key<T>> saveAll(List<T> entity);
    Iterator<T> getAll(Integer limit, Integer offset);
    Iterator<T> getAll();
    List<T> getAllToList(Integer limit, Integer offset);
    List<T> getAllToList();


    UpdateResults updateByField(T entity, Map<String, Object> map);





}
