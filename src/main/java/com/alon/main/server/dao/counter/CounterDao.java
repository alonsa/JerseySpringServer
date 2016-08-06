package com.alon.main.server.dao.counter;

import com.alon.main.server.dao.BaseDao;
import com.alon.main.server.entities.Counter;
import com.alon.main.server.entities.Rating;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

import static com.alon.main.server.Const.Consts.ENTITY_NAME_FIELD;
import static com.alon.main.server.Const.Consts.VALUE_FIELD;

/**
 * Created by alon_ss on 6/26/16.
 */
public interface CounterDao extends BaseDao<Counter> {
    Integer increase(String entityName);
}