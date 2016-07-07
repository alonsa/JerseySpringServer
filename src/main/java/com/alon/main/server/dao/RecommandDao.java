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
public interface RecommandDao<T> {

    public List<T> getByInnerIds(List<Integer> list);
    public T getByInnerId(Integer id);
    public Integer getMaxInnerId();

}
