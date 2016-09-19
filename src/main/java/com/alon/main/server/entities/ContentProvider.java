package com.alon.main.server.entities;

import com.alon.main.server.Const.RecommendedLogicEnum;
import com.beust.jcommander.internal.Sets;
import org.joda.time.DateTime;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import java.util.Set;

/**
 * Created by alon_ss on 6/30/16.
 */

@Entity
public class ContentProvider extends BaseEntity {

    @Indexed(unique = true)
    private String name;

    private String youTubeId;

    private Long lastDataFetch = 0L;

    private RecommendedLogicEnum recomenderLogic ;

    public ContentProvider() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }

    public Long getLastDataFetch() {
        return lastDataFetch;
    }

    public void setLastDataFetch(Long lastDataFetch) {
        this.lastDataFetch = lastDataFetch;
    }

    public RecommendedLogicEnum getRecomenderLogic() {
        return recomenderLogic;
    }

    public void setRecomenderLogic(RecommendedLogicEnum recomenderLogic) {
        this.recomenderLogic = recomenderLogic;
    }
}
