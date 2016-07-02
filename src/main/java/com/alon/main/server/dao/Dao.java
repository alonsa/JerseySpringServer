package com.alon.main.server.dao;

import com.alon.main.server.entities.Movie;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.UpdateResults;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alon_ss on 6/29/16.
 */
public interface Dao<T> {

    public List<T> getByIds(List<ObjectId> list);
    public T getById(ObjectId id);

    public List<T> getByInnerIds(List<Integer> list);
    public T getByInnerId(Integer id);

    public Key<T> save(T entity);
    public Iterable<Key<T>> saveAll(List<T> entity);
    Iterator<T> getAll(Integer limit, Integer offset);
    Iterator<T> getAll();

    public UpdateResults updateByField(T entity, Map<String, Object> map);





}
