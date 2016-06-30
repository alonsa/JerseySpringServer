package com.alon.main.server.dao;

import com.alon.main.server.entities.Movie;

import java.util.List;

/**
 * Created by alon_ss on 6/29/16.
 */
public interface Dao<T> {

    public List<T> getByIds(List<Integer> list);
    public T getById(Integer id);

    }
