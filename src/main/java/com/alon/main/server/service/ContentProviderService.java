package com.alon.main.server.service;

import com.alon.main.server.dao.contentProvider.ContentProviderDao;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.Rating;
import com.alon.main.server.entities.User;
import com.alon.main.server.rest.ContentProviderData;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface ContentProviderService {

    List<ContentProvider> getByIds(List<ObjectId> ids);
    ContentProvider getById(ObjectId id);
    List<ContentProvider> getAll();
    List<Movie> getContentProviderMostPopular(ContentProvider contentProvider);

    ContentProviderData parseYoutubeContentProvider(String youTubeId, boolean force);

}