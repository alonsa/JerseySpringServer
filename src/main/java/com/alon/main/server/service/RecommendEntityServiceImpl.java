package com.alon.main.server.service;

import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.entities.RecommandEntity;
import com.alon.main.server.entities.User;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by alon_ss on 6/26/16.
 */

abstract class RecommendEntityServiceImpl<T extends RecommandEntity>  implements RecommendEntityService<T>{

    @Autowired
    protected CounterService counterService;

    @Autowired
    protected RecommandDao<T> recommandDao;

    public void save(T entity){
        if (entity.getInnerId() == null){
            entity.setInnerId(counterService.increaseEntityCounter(entity.getClass().getSimpleName()));
        }
        recommandDao.save(entity);
    }

    public void saveAll(Iterable<T> list){

        list.forEach(entity -> {
            if (entity.getInnerId() == null){
                entity.setInnerId(counterService.increaseEntityCounter(entity.getClass().getSimpleName()));
            }
        });
        recommandDao.saveAll(list);
    }

    @Override
    public List<T> getByInnerIds(List<Integer> moviesInnerIds) {
        return recommandDao.getOrderedByInnerIds(moviesInnerIds);
    }

    @Override
    public T getByInnerId(Integer moviesInnerId) {
        return recommandDao.getByInnerId(moviesInnerId);
    }
}