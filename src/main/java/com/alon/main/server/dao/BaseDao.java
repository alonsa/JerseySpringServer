package com.alon.main.server.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateResults;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alon_ss on 6/29/16.
 */
public interface BaseDao<T> {

    Class<T> getTypeClass();

    List<T> getByIds(List<ObjectId> list);
    List<T> getOrderedByIds(List<ObjectId> list);

    Long count();

    T getById(ObjectId id);

    @Deprecated
    Iterator<T> getNoUrl(Integer skip);

    Key<T> save(T entity);
    Iterable<Key<T>> saveAll(Iterable<T> entity);
    Iterator<T> getAll(Integer limit, Integer offset);
    Iterator<T> getAll();
    List<T> getAllToList(Integer limit, Integer offset);
    List<T> getAllToList();

    List<T> getQueryList(Query<T> query);
    T getQueryEntity(Query<T> query);

    UpdateResults updateByField(T entity, Map<String, Object> map);





}
