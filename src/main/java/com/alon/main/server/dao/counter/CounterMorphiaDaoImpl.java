package com.alon.main.server.dao.counter;

import com.alon.main.server.dao.BaseDao;
import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.dao.movie.MovieMorphiaDaoImpl;
import com.alon.main.server.dao.user.UserMorphiaDaoImpl;
import com.alon.main.server.entities.Counter;
import com.mongodb.operation.UpdateOperation;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.scalactic.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.*;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public final class CounterMorphiaDaoImpl extends MorphiaBaseDao<Counter> {

    @Override
    public Class<Counter> getTypeClass() {
        return Counter.class;
    }

    @Override
    protected String getDbName() {
        return COUNTER_DB;
    }

    @Autowired
    ApplicationContext applicationContext;


    Map<String, MorphiaBaseDao> nameToDao = new HashMap<>();

    @PostConstruct
    @Async
    protected void init() {
        super.init();

        Collection<MorphiaBaseDao> daos = applicationContext.getBeansOfType(MorphiaBaseDao.class).values();

        for (MorphiaBaseDao dao: daos){
            initCounter(dao);
        }
    }

    public Integer increase(String entityName){
            Query<Counter> findQuery = getQuery().field(ENTITY_NAME_FIELD).equal(entityName);
            UpdateOperations<Counter> updateOperation = datastore.createUpdateOperations(getTypeClass()).inc(VALUE_FIELD);
            Counter counter = datastore.findAndModify(findQuery, updateOperation);

            return counter.getValue();

    }

    private void initCounter(MorphiaBaseDao counterEntityDao) {
        String entityName = counterEntityDao.getTypeClass().getSimpleName();
        Counter counter = getByName(entityName);
        addIfMissing(counterEntityDao, entityName, counter);
    }

    private Counter addIfMissing(MorphiaBaseDao counterEntityDao, String entityName, Counter counter) {
        if (counter == null){
            Long number = Optional.of(counterEntityDao.count()).orElse(0L);
            counter = new Counter(entityName, number.intValue());
            this.save(counter);
        }
        return counter;
    }

    private Counter getAndCreateByName(String name) {
        Counter counter = getByName(name);
        if (nameToDao.containsKey(name)){
            counter = addIfMissing(nameToDao.get(name), name, counter);
        }

        return counter;
    }


    private Counter getByName(String name) {
        Query<Counter> query = getQuery();
        query.field(ENTITY_NAME_FIELD).equal(name);
        return query.get();
    }

}