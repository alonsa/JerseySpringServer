package com.alon.main.server.dao;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by alon_ss on 6/29/16.
 */
public interface RecommandDao<T> extends BaseDao<T>{

    List<T> getByInnerIds(List<Integer> list);
    List<T> getOrderedByInnerIds(List<Integer> list);

    T getByInnerId(Integer id);
    Integer getMaxInnerId();

}
