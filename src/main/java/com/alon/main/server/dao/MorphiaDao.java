package com.alon.main.server.dao;

import com.alon.main.server.entities.BaseEntity;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.alon.main.server.Const.Consts.*;
import static com.mongodb.client.model.Updates.set;

/**
 * Created by alon_ss on 6/29/16.
 */

public abstract class MorphiaDao<T extends BaseEntity> implements Dao<T> {

    // sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db

    private Datastore datastore;

    private Class<T> typeParameterClass = getTypeClass();

    protected abstract Class<T> getTypeClass();
    protected abstract String getDbName();

    @PostConstruct
    @Async
    protected void init() {
        MongoClient mongo = new MongoClient(HOST, PORT);

        Morphia morphia = new Morphia();
        morphia.mapPackage(typeParameterClass.getCanonicalName());

        datastore = morphia.createDatastore(mongo, getDbName());
        morphia.map(typeParameterClass);
        datastore.ensureIndexes();
    }

    @Override
    public List<T> getByIds(List<ObjectId> list) {
        Query<T> query = datastore.get(typeParameterClass, list);
        return query.asList();
    }

    @Override
    public T getById(ObjectId id) {
        return datastore.get(typeParameterClass, id);
    }

    @Override
    public List<T> getByInnerIds(List<Integer> list) {
        Query<T> query = datastore.createQuery(typeParameterClass);
        query.field(INNER_ID_FIELD).in(list);
        return query.asList();
    }

    @Override
    public T getByInnerId(Integer id) {
        Query<T> query = datastore.createQuery(typeParameterClass);
        query.field(INNER_ID_FIELD).equal(id);
        return query.get();
    }

    @Override
    public Key<T> save(T entity) {
        return datastore.save(entity);
    }

    @Override
    public Iterable<Key<T>> saveAll(List<T> entity) {
        return datastore.save(entity);
    }

    @Override
    public Iterator<T> getAll(Integer limit, Integer offset) {
        if (limit == null){
            limit = 0;
        }

        if (offset == null){
            offset = 0;
        }

        return datastore.find(typeParameterClass).offset(offset).limit(limit).fetch().iterator();
    }

    @Override
    public Iterator<T> getAll() {
        return getAll(null, null);
    }

    public UpdateResults updateByField(T entity, Map<String, Object> map) {


        Query<T> query = datastore.createQuery(typeParameterClass).field(ID_FIELD).equal(entity.getId());
        UpdateOperations<T> ops = datastore.createUpdateOperations(typeParameterClass);
        for (Map.Entry<String, Object> entry: map.entrySet()){
            ops.set(entry.getKey(), entry.getValue());
        }

        return datastore.update(query, ops);


    }

}


