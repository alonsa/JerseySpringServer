package com.alon.main.server.dao.contentProvider;

import com.alon.main.server.dao.BaseDao;
import com.alon.main.server.entities.ContentProvider;

/**
 * Created by alon_ss on 9/7/16.
 */
public interface ContentProviderDao extends BaseDao<ContentProvider> {
    ContentProvider findByYouTubeId(String youTubeId);
}
