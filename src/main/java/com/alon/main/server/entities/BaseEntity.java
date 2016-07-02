package com.alon.main.server.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by alon_ss on 6/30/16.
 */
public abstract class BaseEntity {

    @Id
    @Property("id")
    protected ObjectId id;

    public BaseEntity() {
        super();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

}
