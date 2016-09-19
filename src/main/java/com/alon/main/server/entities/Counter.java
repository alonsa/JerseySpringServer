package com.alon.main.server.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by alon_ss on 6/28/16.
 */

@Entity
@XmlRootElement
public class Counter extends ContentProviderEntity implements Serializable{

    @Indexed(unique = true)
    private String entityName;

    private Integer value = 0;

    public Counter(){}

    public Counter(String entityType, Integer value) {
        this.entityName = entityType;
        this.value = value;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}


