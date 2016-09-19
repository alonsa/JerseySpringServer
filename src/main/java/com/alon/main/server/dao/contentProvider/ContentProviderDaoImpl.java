package com.alon.main.server.dao.contentProvider;

import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.dao.MorphiaContentProviderEntityDao;
import com.alon.main.server.dao.MorphiaRecommandDao;
import com.alon.main.server.dao.movie.MovieDao;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import static com.alon.main.server.Const.Consts.*;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public class ContentProviderDaoImpl extends MorphiaBaseDao<ContentProvider> implements ContentProviderDao {

    @Override
    public Class<ContentProvider> getTypeClass() {
        return ContentProvider.class;
    }

    @Override
    protected String getDbName() {
        return CONTENT_PROVIDER_DB;
    }

    @Override
    public ContentProvider findByYouTubeId(String youTubeId) {
        Query<ContentProvider> query = getQuery();
        query.field(YOU_TUBE_ID_FIELD).equal(youTubeId);
        return query.get();
    }
}