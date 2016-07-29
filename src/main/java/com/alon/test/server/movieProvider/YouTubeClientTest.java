package com.alon.test.server.movieProvider;

import com.alon.main.server.movieProvider.YouTubeClientService;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by alon_ss on 7/7/16.
 */
public class YouTubeClientTest {

    private YouTubeClientService testedClass = new YouTubeClientService();

    @Test()
    public void getVodLengthTestForNullValue() {

        Optional<Long> response = testedClass.getVodLength(null);
        Assert.assertTrue(!response.isPresent());
    }

    @Test()
    public void getVodLengthTestForPositiveResponse() {

        String josnString = "{\n" +
                " \"kind\": \"youtube#videoListResponse\",\n" +
                " \"etag\": \"\\\"5g01s4-wS2b4VpScndqCYc5Y-8k/swuiknS-arWoe50Jvqrujt_tlYA\\\"\",\n" +
                " \"pageInfo\": {\n" +
                "  \"totalResults\": 1,\n" +
                "  \"resultsPerPage\": 1\n" +
                " },\n" +
                " \"items\": [\n" +
                "  {\n" +
                "   \"kind\": \"youtube#video\",\n" +
                "   \"etag\": \"\\\"5g01s4-wS2b4VpScndqCYc5Y-8k/4_tqUVzzJZOZndzxCYCx6sXxlHA\\\"\",\n" +
                "   \"id\": \"DhNMHcRSNdo\",\n" +
                "   \"contentDetails\": {\n" +
                "    \"duration\": \"PT13S\",\n" +
                "    \"dimension\": \"2d\",\n" +
                "    \"definition\": \"hd\",\n" +
                "    \"caption\": \"false\",\n" +
                "    \"licensedContent\": true,\n" +
                "    \"projection\": \"rectangular\"\n" +
                "   }\n" +
                "  }\n" +
                " ]\n" +
                "}";

        Long expectedResponse = 13 * 1000L;

        JSONObject responseJson = new JSONObject(josnString);

        testedClass = new YouTubeClientMock(responseJson);

        Optional<Long> vodLength = testedClass.getVodLength("");
        Assert.assertTrue(vodLength.isPresent());
        Assert.assertEquals(vodLength.get(), expectedResponse);
    }

    @Test()
    public void getVodLengthTestForForbiddenResponse() {

        String josnString = "{\n" +
                " \"kind\": \"youtube#videoListResponse\",\n" +
                " \"etag\": \"\\\"5g01s4-wS2b4VpScndqCYc5Y-8k/uXq_sHcCVBhuIv4zZgKSiYsGEtk\\\"\",\n" +
                " \"pageInfo\": {\n" +
                "  \"totalResults\": 1,\n" +
                "  \"resultsPerPage\": 1\n" +
                " },\n" +
                " \"items\": [\n" +
                "  {\n" +
                "   \"kind\": \"youtube#video\",\n" +
                "   \"etag\": \"\\\"5g01s4-wS2b4VpScndqCYc5Y-8k/YgR5C5cF61WfxypSUIbK3_6nCyc\\\"\",\n" +
                "   \"id\": \"cXcH0f2B2vA\",\n" +
                "   \"contentDetails\": {\n" +
                "    \"duration\": \"PT2M2S\",\n" +
                "    \"dimension\": \"2d\",\n" +
                "    \"definition\": \"hd\",\n" +
                "    \"caption\": \"false\",\n" +
                "    \"licensedContent\": true,\n" +
                "    \"regionRestriction\": {\n" +
                "     \"allowed\": [\n" +
                "      \"VI\",\n" +
                "      \"PR\",\n" +
                "      \"US\",\n" +
                "      \"GU\",\n" +
                "      \"AS\",\n" +
                "      \"UM\",\n" +
                "      \"MP\"\n" +
                "     ]\n" +
                "    },\n" +
                "    \"projection\": \"rectangular\"\n" +
                "   }\n" +
                "  }\n" +
                " ]\n" +
                "}";

        Long expectedResponse = 13 * 1000L;

        JSONObject responseJson = new JSONObject(josnString);

        testedClass = new YouTubeClientMock(responseJson);

        Optional<Long> vodLength = testedClass.getVodLength("");
        Assert.assertTrue(!vodLength.isPresent());
    }

    @Test()
    public void getVodLengthTestForEmptyResponse() {

        String josnString = "{}";

        JSONObject responseJson = new JSONObject(josnString);

        testedClass = new YouTubeClientMock(responseJson);

        Optional<Long> vodLength = testedClass.getVodLength("");
        Assert.assertTrue(!vodLength.isPresent());
    }

    private class YouTubeClientMock extends YouTubeClientService {

        JSONObject obj;

        YouTubeClientMock(JSONObject obj){
            this.obj = obj;
        }

        @Override
        protected CompletableFuture<JSONObject> getDataFromYoutube(URI url) {
            return CompletableFuture.completedFuture(obj);
        }


    }


}
