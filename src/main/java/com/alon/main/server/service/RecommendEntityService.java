package com.alon.main.server.service;

import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.RecommandEntity;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface RecommendEntityService<T extends RecommandEntity> {

    void save(T entity);
    void saveAll(Iterable<T> list);
    List<T> getByInnerIds(List<Integer> moviesInnerIds);
    public T getByInnerId(Integer moviesInnerId);

}