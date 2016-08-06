package com.alon.main.server.dao;

import com.alon.main.server.entities.BaseEntity;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/29/16.
 */

public abstract class MorphiaBaseDao<T extends BaseEntity> implements BaseDao<T> {

    private final static Logger logger = Logger.getLogger(MorphiaBaseDao.class);

    // sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db

    protected Datastore datastore;

    private Class<T> typeParameterClass = getTypeClass();

    protected abstract String getDbName();

    @PostConstruct
    @Async
    protected void init() {
        try {
            MongoClient mongo = new MongoClient(HOST, PORT);

            Morphia morphia = new Morphia();
            morphia.mapPackage(typeParameterClass.getCanonicalName());

            datastore = morphia.createDatastore(mongo, getDbName());
            morphia.map(typeParameterClass);

            datastore.ensureIndexes();
        }catch (Exception e){
            logger.error(e);
            logger.error("Some error while tring to access mongoDb. Maybe mongoDb is down. \n Try to run it by: sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db");
        }
    }

    public Long count() {
        return datastore.getCount(getTypeClass());
    }

    protected Query<T> getQuery(){
        return datastore.createQuery(typeParameterClass);
    }

    @Override
    public List<T> getByIds(List<ObjectId> list) {
        Query<T> query = getQuery();
        query.field(ID_FIELD).in(list);

        return query.asList();
    }

    @Override
    public List<T> getOrderedByIds(List<ObjectId> list) {
        List<T> entities = getByIds(list);
        Map<ObjectId, T> idToEntity = entities.stream().collect(Collectors.toMap(T::getId, Function.identity()));
        return list.stream().map(idToEntity::get).collect(Collectors.toList());
    }

    @Override
    public T getById(ObjectId id) {
        return datastore.get(typeParameterClass, id);
    }

    @Deprecated
    public Iterator<T> getNoUrl(Integer skip) {
        Query<T> query = getQuery();
        query.field(URI_FIELD).doesNotExist();
        if (skip != null){
            query = query.offset(skip);
        }
        return query.iterator();
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
        return getAllQuery(limit, offset).fetch().iterator();
    }

    @Override
    public Iterator<T> getAll() {
        return getAll(null, null);
    }

    @Override
    public List<T> getAllToList(Integer limit, Integer offset) {
        return getAllQuery(limit, offset).asList();
    }

    @Override
    public List<T> getAllToList() {
        return getAllToList(null, null) ;
    }

    @Override
    public UpdateResults updateByField(T entity, Map<String, Object> map) {


        Query<T> query = getQuery().field(ID_FIELD).equal(entity.getId());
        UpdateOperations<T> ops = datastore.createUpdateOperations(typeParameterClass);
        for (Map.Entry<String, Object> entry: map.entrySet()){
            ops.set(entry.getKey(), entry.getValue());
        }

        return datastore.update(query, ops);
    }

    private Query<T> getAllQuery(Integer limit, Integer offset) {
        if (limit == null){
            limit = 0;
        }

        if (offset == null){
            offset = 0;
        }

        return datastore.find(typeParameterClass).offset(offset).limit(limit);
    }


}


