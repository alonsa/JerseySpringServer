package com.alon.main.server.service;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.Const.RecommendedLogicEnum;
import com.alon.main.server.dao.contentProvider.ContentProviderDao;
import com.alon.main.server.entities.BaseEntity;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.YouTubeClientServiceImpl;
import com.alon.main.server.rest.ContentProviderData;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.math3.util.Pair;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.joda.time.*;
import org.joda.time.base.BaseDateTime;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class ContentProviderServiceImpl implements ContentProviderService {

    private final static Logger logger = Logger.getLogger(ContentProviderServiceImpl.class);

    private final YouTubeClientServiceImpl youTubeClient;

    private final ContentProviderDao contentProviderDao;

    private final MovieService movieService;

    private static StopWatch stopWatch = new StopWatch();


    @Autowired
    public ContentProviderServiceImpl(MovieService movieService, ContentProviderDao contentProviderDao, YouTubeClientServiceImpl youTubeClient) {
        this.movieService = movieService;
        this.contentProviderDao = contentProviderDao;
        this.youTubeClient = youTubeClient;

        logger.debug("#############################################");
        logger.debug("###   ContentProviderServiceImpl is up!   ###");
        logger.debug("#############################################");
    }

    @Scheduled(fixedRate=3600000) // One hour
    public void doSomething() {

        stopWatch.reset();
        stopWatch.start();

        List<ContentProvider> contentProviderList = getAll();

        List<String> contentProvideYoutubeId = contentProviderList.stream()
                .map(ContentProvider::getYouTubeId)
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        logger.debug("About to parse Youtube contentProvider data for channels Ids:" + contentProvideYoutubeId.stream().collect(Collectors.joining(", ")));

        List<ContentProviderData> contentProviderDataList = contentProvideYoutubeId.parallelStream().map(x -> parseYoutubeContentProvider(x, false)).collect(Collectors.toList());


        List<ContentProviderData> filteredContentProviderDataList = contentProviderDataList.stream()
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        filteredContentProviderDataList.forEach(this::toLog);

        logger.debug("The operation toke : " + stopWatch.getTime() / 1000 + " seconds" + "\n");

        stopWatch.reset();
    }

    private void toLog(ContentProviderData data) {
        StringBuilder sb = new StringBuilder();

        sb.append("Get content provider data for youTubeId: ").append(data.getYouTubeId()).append("\n");
        if (data.isNew()){
            sb.append("A new ");
        }

        sb.append("content provider with name : ").append(data.getName()).append("\n");
        sb.append("parsed : ").append(data.getVodNumber()).append(" new vods").append("\n");

        logger.debug(sb.toString());
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
    public List<Movie> getContentProviderMostPopular(ContentProvider contentProvider){

        Set<Movie> contentProviderVods = getContentProviderVods(contentProvider, true, false, Optional.of(10));
        Optional<Movie> oldestVods = contentProviderVods.stream().sorted((x, y) -> x.getPublishDate().compareTo(y.getPublishDate())).findFirst();


        Optional<Duration> ddd = oldestVods
                .map(Movie::getPublishDate)
                .map(x -> new Duration(new DateTime().getMillis() - oldestVods.get().getPublishDate()));

        // Get duration from the oldest vod. and multiply it by to
        Long fetchDate = oldestVods
                .map(Movie::getPublishDate)
                .map(DateTime::new)
                .map(BaseDateTime::getMillis)
                .orElse(0L);

        List<String> mostPopularVodIds = youTubeClient.getContentProviderMostPopularVodIds(contentProvider.getYouTubeId(), fetchDate, Optional.of(20));
        List<Movie> mostPopularVods = movieService.findByExternalSiteIds(MovieSite.YOU_TUBE, mostPopularVodIds);

        Map<String, Movie> mostPopularTubeIdToVoaMap = mostPopularVods.stream().collect(Collectors.toMap(x -> x.getExternalSiteToId().get(MovieSite.YOU_TUBE), x -> x));

        return mostPopularVodIds.stream()
                .map(mostPopularTubeIdToVoaMap::get)
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public ContentProviderData parseYoutubeContentProvider(String youTubeId, boolean force) {

        ContentProvider contentProvider = contentProviderDao.findByYouTubeId(youTubeId);

        Boolean isContentProviderCreated = false;

        if (contentProvider == null){
            logger.debug("No existing content provider for youTubeId: " + youTubeId + ", Going to create one");
            Optional<ContentProvider> contentProviderOptional = youTubeClient.getContentProviderByChannelId(youTubeId);
            if (contentProviderOptional.isPresent()){
                contentProvider = contentProviderOptional.get();
                contentProvider.setRecomenderLogic(RecommendedLogicEnum.RELATED);
                Key<ContentProvider> key = contentProviderDao.save(contentProviderOptional.get());
                contentProvider.setId((ObjectId) key.getId());
                isContentProviderCreated = true;
            }
        }

        ContentProviderData contentProviderData = null;

        if (contentProvider != null){

            Set<Movie> contentProviderVods = getContentProviderVods(contentProvider, force, true, Optional.empty());

            contentProviderData = new ContentProviderData(contentProvider.getYouTubeId(), contentProvider.getName(), isContentProviderCreated, contentProviderVods.size());


        }else{
            logger.warn("No content provider for youTubeId: " + youTubeId);
        }

        return contentProviderData;
    }

    private Set<Movie> getContentProviderVods(ContentProvider contentProvider, boolean force, boolean needToSave, Optional<Integer> maxSize){
        // get all channel vods youTube ids
        Set<String> vodYouTubeIds = getContentProviderIds(contentProvider, force, needToSave, maxSize);
        return getYouTubeVodsByIds(contentProvider, vodYouTubeIds);
    }

    private Set<String> getContentProviderIds(ContentProvider contentProvider, boolean force, boolean needToSave, Optional<Integer> maxSize){

        Long lastDataFetch = force ? 0 : contentProvider.getLastDataFetch();

        if (needToSave){
            contentProvider.setLastDataFetch(DateTime.now().getMillis());
            contentProviderDao.save(contentProvider);
        }
        // get all channel vods youTube ids
        return youTubeClient.getContentProviderVodIds(contentProvider.getYouTubeId(), lastDataFetch, maxSize);
    }

    private Set<Movie> getYouTubeVodsByIds(ContentProvider contentProvider, Set<String> vodYouTubeIds) {
        List<Movie> dbVods = movieService.findByExternalSiteIds(MovieSite.YOU_TUBE, vodYouTubeIds);
        Set<String> dbVodYouTubeIds = dbVods.stream().map(x -> x.getExternalSiteToId().get(MovieSite.YOU_TUBE)).collect(Collectors.toSet());
        vodYouTubeIds.removeAll(dbVodYouTubeIds);

        // get all channel vods youTube details
        Set<Movie> contentProviderVods = youTubeClient.getVideoDetails(vodYouTubeIds);

        // add for each vod channel Id
        contentProviderVods.forEach(vod -> vod.setCpId(contentProvider.getId()));

        // save vods
        movieService.saveAll(contentProviderVods);

        contentProviderVods.addAll(dbVods);

        addRelatedVods(contentProviderVods, contentProvider.getYouTubeId());

        return contentProviderVods;
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