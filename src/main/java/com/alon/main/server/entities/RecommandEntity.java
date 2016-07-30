package com.alon.main.server.entities;

import org.mongodb.morphia.annotations.Indexed;

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
