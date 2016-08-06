package com.alon.main.server.dao.rating;

import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.entities.Rating;
import org.springframework.stereotype.Service;

import static com.alon.main.server.Const.Consts.RATING_DB;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public class RatingMorphiaDaoImpl extends MorphiaBaseDao<Rating> implements RatingDao{

    @Override
    public Class<Rating> getTypeClass() {
        return Rating.class;
    }

    @Override
    protected String getDbName() {
        return RATING_DB;
    }

}