package com.alon.main.server.service;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.dao.contentProvider.ContentProviderDao;
import com.alon.main.server.entities.BaseEntity;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.YouTubeClientServiceImpl;
import org.apache.commons.math3.util.Pair;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class ContentProviderServiceImpl implements ContentProviderService {

    private final static Logger logger = Logger.getLogger(ContentProviderServiceImpl.class);

    private final YouTubeClientServiceImpl youTubeClient;

    private final ContentProviderDao contentProviderDao;

    private final MovieService movieService;

    @Autowired
    public ContentProviderServiceImpl(MovieService movieService, ContentProviderDao contentProviderDao, YouTubeClientServiceImpl youTubeClient) {
        this.movieService = movieService;
        this.contentProviderDao = contentProviderDao;
        this.youTubeClient = youTubeClient;

        logger.debug("#############################################");
        logger.debug("###   ContentProviderServiceImpl is up!   ###");
        logger.debug("#############################################");
    }

    @Override
    public List<ContentProvider> getByIds(List<ObjectId> ids) {
        return contentProviderDao.getOrderedByIds(ids);
    }

    @Override
    public ContentProvider getById(ObjectId id) {
        return contentProviderDao.getById(id);
    }

    @Override
    public List<ContentProvider> getAll() {
        return contentProviderDao.getAllToList();
    }

    @Override
    public void parseYoutubeContentProvider(String youTubeId) {

        ContentProvider contentProvider = contentProviderDao.findByYouTubeId(youTubeId);

        if (contentProvider == null){
            logger.debug("No existing content provider for youTubeId: " + youTubeId + ", Going to create one");
            Optional<ContentProvider> contentProviderOptional = youTubeClient.getContentProviderByChannelId(youTubeId);
            if (contentProviderOptional.isPresent()){
                contentProvider = contentProviderOptional.get();
                Key<ContentProvider> key = contentProviderDao.save(contentProviderOptional.get());
                contentProvider.setId((ObjectId) key.getId());
            }
        }

        if (contentProvider != null){
            ObjectId contentProviderId = contentProvider.getId();

            Long lastDataFetch = contentProvider.getLastDataFetch();
            contentProvider.setLastDataFetch(DateTime.now().getMillis());
            contentProviderDao.save(contentProvider);

            // get all channel vods youTube ids
            Set<String> vodYouTubeIds = youTubeClient.getContentProviderVods(contentProvider, lastDataFetch);

            List<Movie> dbVods = movieService.findByExternalSiteIds(MovieSite.YOU_TUBE, vodYouTubeIds);
            Set<String> dbVodYouTubeIds = dbVods.stream().map(x -> x.getExternalSiteToId().get(MovieSite.YOU_TUBE)).collect(Collectors.toSet());
            vodYouTubeIds.removeAll(dbVodYouTubeIds);

            // get all channel vods youTube details
            Set<Movie> contentProviderVods = youTubeClient.getVideoDetails(vodYouTubeIds);

            // add for each vod channel Id
            contentProviderVods.forEach(vod -> vod.setCpId(contentProviderId));

            // save vods
            movieService.saveAll(contentProviderVods);

            contentProviderVods.addAll(dbVods);

            addRelatedVods(contentProviderVods, youTubeId);

        }else{
            logger.warn("No content provider for youTubeId: " + youTubeId);
        }
    }

    private void addRelatedVods(Set<Movie> contentProviderVods, String contentProviderYouTubeId) {

        // create for each vod its related vods
        Map<Movie, List<String>> vodYouTubeIdToRelatedMap = contentProviderVods.parallelStream().map(vod -> {
            String vodYouTubeId = vod.getExternalSiteToId().get(MovieSite.YOU_TUBE);
            List<String> related = youTubeClient.getRelatedVideos(vodYouTubeId, contentProviderYouTubeId);
            return Pair.create(vod, related);
        }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        Set<String> relatesYouTubeIds = vodYouTubeIdToRelatedMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

        List<Movie> dbRelatedVods = movieService.findByExternalSiteIds(MovieSite.YOU_TUBE, relatesYouTubeIds);

        Map<String, ObjectId>  vodYouTubeIdToVodIdMap = dbRelatedVods.stream().collect(Collectors.toMap(vod -> vod.getExternalSiteToId().get(MovieSite.YOU_TUBE) , BaseEntity::getId));

        vodYouTubeIdToRelatedMap.entrySet().forEach(x -> {
            Movie vod = x.getKey();
            List<String> relatedYouTubeIds = x.getValue();
            List<ObjectId> relatedIds = relatedYouTubeIds.stream().map(youTubeId -> Optional.ofNullable(vodYouTubeIdToVodIdMap.get(youTubeId))).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            vod.setRelatedVods(relatedIds);
        });

        movieService.saveAll(vodYouTubeIdToRelatedMap.keySet());

    }


}