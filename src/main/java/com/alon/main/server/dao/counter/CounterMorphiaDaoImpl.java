package com.alon.main.server.dao.counter;

import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.dao.user.UserDao;
import com.alon.main.server.entities.Counter;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public class CounterMorphiaDaoImpl extends MorphiaBaseDao<Counter> implements CounterDao {

    private UserDao userDao;

    @Autowired
    public CounterMorphiaDaoImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Class<Counter> getTypeClass() {
        return Counter.class;
    }

    @Override
    protected String getDbName() {
        return COUNTER_DB;
    }

    private Map<String, RecommandDao> nameToDao = new HashMap<>();

    @PostConstruct
    protected void init() {
        super.init();
        initCounter(userDao);
    }

    public Integer increase(String entityName){
            Query<Counter> findQuery = getQuery().field(ENTITY_NAME_FIELD).equal(entityName);
            UpdateOperations<Counter> updateOperation = datastore.createUpdateOperations(getTypeClass()).inc(VALUE_FIELD);
            Counter counter = datastore.findAndModify(findQuery, updateOperation);

            return counter.getValue();
    }

    private void initCounter(RecommandDao counterEntityDao) {
        String entityName = counterEntityDao.getTypeClass().getSimpleName();
        Counter counter = getByName(entityName);
        addIfMissing(counterEntityDao, entityName, counter);
    }

    private Counter addIfMissing(RecommandDao counterEntityDao, String entityName, Counter counter) {
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