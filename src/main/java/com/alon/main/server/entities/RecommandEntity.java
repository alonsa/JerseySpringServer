package com.alon.main.server.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

import javax.xml.bind.annotation.XmlRootElement;

import static com.alon.main.server.Const.Consts.ID_FIELD;

/**
 * Created by alon_ss on 6/30/16.
 */
public abstract class RecommandEntity extends BaseEntity{

    @Indexed(unique = true)
    private Integer innerId;

    public Integer getInnerId() {
        return innerId;
    }

    public void setInnerId(Integer innerId) {
        this.innerId = innerId;
    }

}
