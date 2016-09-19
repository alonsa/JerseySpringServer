package com.alon.main.server.service;

import com.alon.main.server.Const.MovieSite;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.movieProvider.TmdbClientService;
import com.alon.main.server.movieProvider.YouTubeClientServiceImpl;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.alon.main.server.Const.MovieSite.TMDB;
import static com.alon.main.server.Const.MovieSite.YOU_TUBE;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
@Deprecated
public class IntooiTVMockServiceImpl implements IntooiTVMockService {

    // This service is for tests purpose only and need to delete
    private final static Logger logger = Logger.getLogger(IntooiTVMockServiceImpl.class);

    private final static String uriTemplate = "https://www.youtube.com/embed/%s?autoplay=1";

    private MovieService movieService;

    private TmdbClientService tmdbClient;

    private YouTubeClientServiceImpl youTubeClient;

    @Autowired
    public IntooiTVMockServiceImpl(YouTubeClientServiceImpl youTubeClient, TmdbClientService tmdbClient, MovieService movieService) {
        this.youTubeClient = youTubeClient;
        this.tmdbClient = tmdbClient;
        this.movieService = movieService;

        logger.debug("######################################");
        logger.debug("###   IntooiTVMockService is up!   ###");
        logger.debug("######################################");
    }

    public Movie fillMovieData(Movie movie){

        Map<MovieSite, String> externalSiteToId = movie.getExternalSiteToId();

        boolean isNeedToSave = false;

        // Set ExternalInfoToMap
        if (movie.getExternalSiteToId().size() != movie.getExternalIds().size()){
            copyExternalInfoToMap(movie);
            isNeedToSave = true;
        }

        // Set URI
        if (movie.getUri() != null && !externalSiteToId.containsKey(YOU_TUBE)){
            externalSiteToId.put(YOU_TUBE, getYouTubeId(movie.getUri()));
            isNeedToSave = true;
        }

        if (isNeedToSave){
            movieService.save(movie);
        }

        boolean isRealData = true;
        isNeedToSave = false;

        // Get default URL if absent
        if (movie.getUri() == null){
            setDefaultData(movie);
            isRealData = false;
        }

        Optional<Movie> youTubeDataMovie = getDataFromYouTube(movie);
        if (youTubeDataMovie.isPresent() && !youTubeDataMovie.map(Movie::isForbidden).get()){
            movie.setLength(youTubeDataMovie.map(Movie::getLength).get());
        }else{
            setDefaultData(movie);
            isRealData = false;
        }

        if (movie.getPlot() == null){
            Optional<String> optionalTmdbId = Optional.ofNullable(movie.getExternalSiteToId().get(TMDB));

            if (!optionalTmdbId.isPresent()){
                logger.warn("Movie has no TmdbId. " + movie);
                setDefaultPlot(movie);
                isRealData = false;
            }else{
                Optional<String> overviewOption = Optional.empty();
                try {
                    overviewOption = tmdbClient.getFutureOverview(optionalTmdbId.orElse(null));
                } catch (ExecutionException | InterruptedException ignored) {
                }
                movie.setPlot(overviewOption.orElse(getDefaultPlot(movie)));
                isNeedToSave = true;
            }
        }

        if (isRealData && isNeedToSave){
            movieService.save(movie);
        }
        return movie;
    }

    @Override
    public Movie changeYouTubeUri(Movie movie) {

        String uri = String.format(uriTemplate, movie.getExternalSiteToId().get(MovieSite.YOU_TUBE));
        movie.setUri(URI.create(uri));

        return movie;

    }

    private void copyExternalInfoToMap(Movie movie) {
        for (ExternalId externalId: movie.getExternalIds()){
            movie.getExternalSiteToId().put(externalId.getSiteName(), externalId.getId());
        }
    }

    public String getYouTubeId(URI uri){

        String youTubeId = null;
        if (uri != null){
            List<NameValuePair> params = URLEncodedUtils.parse(uri, "UTF-8");
            Optional<String> youTubeIdOption = params.stream().filter(x -> x.getName().equals("v")).findFirst().map(NameValuePair::getValue);
            youTubeId = youTubeIdOption.orElse(null);
        }
        return youTubeId;
    }

    private Optional<Movie> getDataFromYouTube(Movie movie) {
        String youTubeId = movie.getExternalSiteToId().get(YOU_TUBE);
        return youTubeClient.getVideoDetails(youTubeId);
    }

    private void setDefaultData(Movie movie) {

        Map<URI, Long> urisToLength = getUrisToLengthMap();

        int uriIndex = (Math.abs(movie.hashCode()) % urisToLength.size());

        List<URI> urls = new ArrayList<>();

        urls.addAll(urisToLength.keySet());
        URI defaultUri = urls.get(uriIndex);
        Long defaultUriLength = urisToLength.get(defaultUri);

        movie.setLength(defaultUriLength);
        movie.setUri(defaultUri);
        movie.getExternalSiteToId().put(YOU_TUBE, getYouTubeId(movie.getUri()));

        setDefaultPlot(movie);
    }

    private void setDefaultPlot(Movie movie) {
        String getDefaultPlot = getDefaultPlot(movie);
        movie.setPlot(getDefaultPlot);
    }

    private String getDefaultPlot(Movie movie) {
        List<String> plots = getDefaultPlots();
        int index = (Math.abs(movie.hashCode()) % plots.size());

        return plots.get(index);
    }

    private static Map<URI, Long> getUrisToLengthMap(){
        Map<URI, Long> urisToLength = new HashMap<>();
        try {
            urisToLength.put(new URI("https://www.youtube.com/watch?v=LTgRm6Qgscc"), 25000L);
            urisToLength.put(new URI("https://www.youtube.com/watch?v=DhNMHcRSNdo"), 13000L);
            urisToLength.put(new URI("https://www.youtube.com/watch?v=hvha-7EvwNg"), 31000L);
            urisToLength.put(new URI("https://www.youtube.com/watch?v=kvg9GxWjgIw"), 14000L);
            urisToLength.put(new URI("https://www.youtube.com/watch?v=OT9HsNszYCI"), 15000L);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return urisToLength;
    }

    private static List<String> getDefaultPlots(){
        String plot = "Currently plot is missing: ";
        List<String> plots = new ArrayList<>();
        plots.add(plot + "A hero from humble beginnings gains the thing that he or she wants – money, power, a partner –" +
                " before losing it and having to fight to get it back again.");

        plots.add(plot + "The main character travels to an unfamiliar place, meeting new characters and overcoming a series of trials," +
                " all the while trying to get home. Their new friendships and newfound wisdom allow them to find their way back again.");

        plots.add(plot + "The hero sets out in search of a specific prize, " +
                "overcoming a series of challenges and temptations. " +
                "They may have flaws which have held them back in the past which they will need to overcome to succeed.");

        plots.add(plot + "The main character is a bad or unpleasant person who is shown the error of their ways and redeems themself over the course of the story.");

        plots.add(plot + "The villain has left the hero for dead, " +
                "or killed the hero’s brother, sister, parents, wife or family pets." +
                " Filled with righteous fury, the hero tools up and embarks on a bloody rampage.");

        return plots;

    }


}