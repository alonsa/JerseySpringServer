package com.alon.main.server.dao.movie;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.dao.MorphiaRecommandDao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alon.main.server.Const.Consts.EXTERNAL_SITE_TO_ID_FIELD;
import static com.alon.main.server.Const.Consts.NAME_FIELD;
import static com.alon.main.server.Const.Consts.VOD_DB;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public class MovieMorphiaDaoImpl extends MorphiaRecommandDao<Movie> implements MovieDao {

    @Override
    public Class<Movie> getTypeClass() {
        return Movie.class;
    }

    @Override
    protected String getDbName() {
        return VOD_DB;
    }

    @Override
    public List<Movie> findByExternalSiteIds(MovieSite site, Iterable<String> externalId) {
        Query<Movie> query = getQuery();
        query.field(EXTERNAL_SITE_TO_ID_FIELD + "." + site.name()).in(externalId);
        return query.asList();
    }

    @Override
    public Movie findByExternalSiteId(MovieSite site, String externalId) {
        Query<Movie> query = getQuery();
        query.field(EXTERNAL_SITE_TO_ID_FIELD + "." + site.name()).equal(externalId);
        return query.get();
    }
}