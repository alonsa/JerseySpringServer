package com.alon.main.server.dao.rating;

import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.entities.Counter;
import com.alon.main.server.entities.Rating;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public final class RatingMorphiaDaoImpl extends MorphiaBaseDao<Rating> {

    @Override
    public Class<Rating> getTypeClass() {
        return Rating.class;
    }

    @Override
    protected String getDbName() {
        return RATING_DB;
    }

}