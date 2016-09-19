package com.alon.test.server.movieProvider;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ContentProvider;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.YouTubeClientServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by alon_ss on 7/7/16.
 */
public class YouTubeClientServiceImplTest {

    private YouTubeClientServiceImpl testedClass = new YouTubeClientServiceImpl();

    @Test()
    public void getVideoDetailsFullResponse() {

        String jsonString = Jsons.videoDetailsFullJsonString;

        Long expectedResponse = 132000L;

        JSONObject responseJson = new JSONObject(jsonString);

        testedClass = new YouTubeClientMockImpl(responseJson);

        Set<Movie> vods = testedClass.getVideoDetails(Sets.newHashSet());

        Assert.assertTrue(!vods.isEmpty());
        Movie testedVods = vods.iterator().next();
        Assert.assertEquals(expectedResponse, testedVods.getLength());
        Assert.assertTrue(!testedVods.getExternalSiteToId().isEmpty());
        Assert.assertEquals("d4HO9UY9Mb0", testedVods.getExternalSiteToId().get(MovieSite.YOU_TUBE));
        Assert.assertEquals("title", testedVods.getPlot());
        Assert.assertEquals("title", testedVods.getTitle());
        Assert.assertEquals((Long)1453061269000L, testedVods.getPublishDate());
    }

    @Test()
    public void getVodLengthTestForNoItemsResponse() {

        String jsonString = Jsons.vodLengthTestForNoItemsJsonString;

        JSONObject responseJson = new JSONObject(jsonString);
        testedClass = new YouTubeClientMockImpl(responseJson);
        Set<Movie> movies = testedClass.getVideoDetails(Sets.newHashSet());

        Assert.assertTrue(movies.isEmpty());
    }

    @Test()
    public void getVodLengthTestForEmptyResponse() {

        String jsonString = Jsons.emptyJsonString;

        JSONObject responseJson = new JSONObject(jsonString);
        testedClass = new YouTubeClientMockImpl(responseJson);
        Set<Movie> movies = testedClass.getVideoDetails(Sets.newHashSet());

        Assert.assertTrue(movies.isEmpty());
    }

    @Test
    public void getContentProviderByChannelIdTest(){

        String jsonString = Jsons.ContentProviderByChannelIdJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        testedClass = new YouTubeClientMockImpl(responseJson);

        Optional<ContentProvider> contentProviderOption = testedClass.getContentProviderByChannelId("UCbcovN-W9s9bkoe0wqIEjGg");

        ContentProvider testedContentProvider = new ContentProvider();
        testedContentProvider.setName("channelTitle");
        testedContentProvider.setYouTubeId("UCbcovN-W9s9bkoe0wqIEjGg");

        Assert.assertTrue(contentProviderOption.isPresent());
        Assert.assertEquals(testedContentProvider.getName(), contentProviderOption.get().getName());
        Assert.assertEquals(testedContentProvider.getYouTubeId(), contentProviderOption.get().getYouTubeId());
    }

    @Test
    public void getContentProviderByChannelIdNoSnippetTest(){

        String jsonString = Jsons.ContentProviderByChannelIdNoSnippetJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        testedClass = new YouTubeClientMockImpl(responseJson);

        Optional<ContentProvider> contentProviderOption = testedClass.getContentProviderByChannelId("UCbcovN-W9s9bkoe0wqIEjGg");

        Assert.assertTrue(!contentProviderOption.isPresent());
    }

    @Test
    public void getContentProviderByChannelIdNoItemsTest(){

        String jsonString = Jsons.ContentProviderByChannelIdNoItemsJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        testedClass = new YouTubeClientMockImpl(responseJson);

        Optional<ContentProvider> contentProviderOption = testedClass.getContentProviderByChannelId("UCbcovN-W9s9bkoe0wqIEjGg");

        Assert.assertTrue(!contentProviderOption.isPresent());
    }

    @Test
    public void getChannelVodsForFullData(){
        String jsonString = Jsons.ChannelVodsForFullDataJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        testedClass = new YouTubeClientMockImpl(responseJson);
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setYouTubeId("");
        Long lastDataFetch = contentProvider.getLastDataFetch();
        contentProvider.setLastDataFetch(DateTime.now().getMillis());
        Set<String> contentProviderVods = testedClass.getContentProviderVods(contentProvider, lastDataFetch);

        HashSet<String> testedSet = Sets.newHashSet("7aG50_Gg6oE");

        Assert.assertTrue(!contentProviderVods.isEmpty());
        Assert.assertEquals(contentProviderVods, testedSet);
    }

    @Test
    public void getChannelVodsForSlittedData(){

        String jsonString1 = Jsons.ChannelVodsForSlittedDataJsonString1;

        String jsonString2 = Jsons.ChannelVodsForSlittedDataJsonString2;

        JSONObject responseJson1 = new JSONObject(jsonString1);
        JSONObject responseJson2 = new JSONObject(jsonString2);
        ArrayList<JSONObject> jsonList = Lists.newArrayList(responseJson1, responseJson2);

        testedClass = new YouTubeClientMockImpl(jsonList);
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setYouTubeId("");
        Long lastDataFetch = contentProvider.getLastDataFetch();
        contentProvider.setLastDataFetch(DateTime.now().getMillis());
        Set<String> contentProviderVods = testedClass.getContentProviderVods(contentProvider, lastDataFetch);

        HashSet<String> testedSet = Sets.newHashSet("d4HO9UY9Mb0", "7aG50_Gg6oE", "5PLx-kNkVxA", "18RXuWMyHNI", "sp6n6bOxyPo");

        Assert.assertTrue(!contentProviderVods.isEmpty());
        Assert.assertEquals(contentProviderVods, testedSet);

    }

    @Test
    public void filterJsonObjectTest(){
        String jsonString = Jsons.filterJsonObjectString;

        JSONObject responseJson = new JSONObject(jsonString);

        Stack<String> path = new Stack<String>();
        path.add("channelId");
        path.add("snippet");

        HashMap<String, Stack<String>> map = Maps.newHashMap();
        map.put("testChannelId", path);

        boolean isValid = testedClass.filterJsonObject(responseJson, map);

        Assert.assertTrue(isValid);
    }

    @Test
    public void filterJsonObjectNoValueTest(){

        String jsonString = Jsons.filterJsonObjectNoValueJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        Stack<String> path = new Stack<String>();
        path.add("channelId");
        path.add("snippet");

        HashMap<String, Stack<String>> map = Maps.newHashMap();
        map.put("testChannelId", path);

        boolean isValid = testedClass.filterJsonObject(responseJson, map);

        Assert.assertTrue(!isValid);
    }

    @Test
    public void filterJsonObjectNoChannelIdTest(){
        String jsonString = Jsons.filterJsonObjectNoChannelIdJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        Stack<String> path = new Stack<String>();
        path.add("channelId");
        path.add("snippet");

        HashMap<String, Stack<String>> map = Maps.newHashMap();
        map.put("testChannelId", path);

        boolean isValid = testedClass.filterJsonObject(responseJson, map);

        Assert.assertTrue(!isValid);
    }

    @Test
    public void filterJsonObjectNoSnippetTest(){
        String jsonString = Jsons.filterJsonObjectNoSnippetJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        Stack<String> path = new Stack<String>();
        path.add("channelId");
        path.add("snippet");

        HashMap<String, Stack<String>> map = Maps.newHashMap();
        map.put("testChannelId", path);

        boolean isValid = testedClass.filterJsonObject(responseJson, map);

        Assert.assertTrue(!isValid);
    }

    @Test
    public void filterJsonObjectNullFilterTest(){
        String jsonString = Jsons.filterJsonObjectString;

        JSONObject responseJson = new JSONObject(jsonString);

        boolean isValid = testedClass.filterJsonObject(responseJson, null);

        Assert.assertTrue(isValid);
    }

    @Test
    public void filterJsonObjectEmptyJsonNoFilterTest(){
        String jsonString = Jsons.emptyJsonString;

        JSONObject responseJson = new JSONObject(jsonString);

        Stack<String> path = new Stack<String>();
        path.add("channelId");
        path.add("snippet");

        HashMap<String, Stack<String>> map = Maps.newHashMap();
        map.put("testChannelId", path);
        boolean isValid = testedClass.filterJsonObject(responseJson, map);

        Assert.assertTrue(!isValid);
    }


    @Test()
    public void getRelatedVideosTest() {
        String jsonString1 = Jsons.RelatedVideosJsonString1;
        String jsonString2 = Jsons.RelatedVideosJsonString2;

        JSONObject responseJson1 = new JSONObject(jsonString1);
        JSONObject responseJson2 = new JSONObject(jsonString2);

        testedClass = new YouTubeClientMockImpl(Lists.newArrayList(responseJson1, responseJson2));

        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(testedClass, "RELATED_NUM", 5);

        List<String> relatedVodIds = testedClass.getRelatedVideos("relatedVodId", "UCbcovN-W9s9bkoe0wqIEjGg");
        List<String> expectedIds = Lists.newArrayList("7aG50_Gg6oE", "5PLx-kNkVxA");

        Assert.assertTrue(!relatedVodIds.isEmpty());
        Assert.assertEquals(relatedVodIds, expectedIds);
    }

    @Test()
    public void getRelatedVideosMaxReturnTest() {
        String jsonString1 = Jsons.RelatedVideosJsonString1;
        String jsonString2 = Jsons.RelatedVideosJsonString2;

        JSONObject responseJson1 = new JSONObject(jsonString1);
        JSONObject responseJson2 = new JSONObject(jsonString2);

        testedClass = new YouTubeClientMockImpl(Lists.newArrayList(responseJson1, responseJson2));

        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(testedClass, "RELATED_NUM", 1);

        List<String> relatedVodIds = testedClass.getRelatedVideos("relatedVodId", "UCbcovN-W9s9bkoe0wqIEjGg");
        List<String> expectedIds = Lists.newArrayList("7aG50_Gg6oE", "5PLx-kNkVxA");

        Assert.assertTrue(!relatedVodIds.isEmpty());
        Assert.assertEquals(relatedVodIds, expectedIds);
    }

    private class YouTubeClientMockImpl extends YouTubeClientServiceImpl {

        List<JSONObject> list = Lists.newArrayList();
        Integer index = 0;

        YouTubeClientMockImpl(JSONObject obj){
            list.add(obj);
        }

        YouTubeClientMockImpl(List<JSONObject> obj){
            list.addAll(obj);
        }

        private JSONObject getElement(){
            if (index.equals(list.size())){
                index = 0;
            }
            return list.get(index++);
        }

        @Override
        protected CompletableFuture<JSONObject> getDataFromYoutube(URI url) {
            return CompletableFuture.completedFuture(getElement());
        }
    }

}
