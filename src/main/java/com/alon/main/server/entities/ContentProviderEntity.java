package com.alon.main.server.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.NotSaved;

import javax.validation.constraints.NotNull;

/**
 * Created by alon_ss on 6/30/16.
 */
public abstract class ContentProviderEntity extends BaseEntity {

    @NotNull
    private ObjectId cpId;

    ContentProviderEntity() {
        super();
    }

    public ObjectId getCpId() {
        return cpId;
    }

    public void setCpId(ObjectId cpId) {
        this.cpId = cpId;
    }
}
