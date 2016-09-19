package com.alon.main.server.rest;

import com.alon.main.server.entities.CurrentlyWatch;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import com.alon.main.server.service.EpgService;
import com.alon.main.server.service.IntooiTVMockService;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.RESPONSE_NUM;

@Path("/recommend/")
@Component
public class EpgRestServiceImpl {

//		http://localhost:8090/recommend/epg/9021
//		http://localhost:8090/recommend/epgs/9021

	private final static Logger logger = Logger.getLogger(EpgRestServiceImpl.class);

	private EpgService epgService;

	private IntooiTVMockService intooiTVMockService;

	private static StopWatch stopWatch = new StopWatch();

	@DefaultValue(RESPONSE_NUM) @QueryParam("num") Integer askedMovieNum;
	@QueryParam("play") String play;
	@QueryParam("type") String type;
	@DefaultValue("true") @QueryParam("like") boolean likeCurrentlyPlay;

	@Autowired
	public EpgRestServiceImpl(EpgService epgService, IntooiTVMockService intooiTVMockService) {

		this.epgService = epgService;
		this.intooiTVMockService = intooiTVMockService;

		logger.debug("####################################");
		logger.debug("###   EpgRestServiceImpl is up!  ###");
		logger.debug("####################################");

	}

	@GET
	@Path("epgs/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public List<Epg> recommands(@PathParam("id") String userName) {
		logger.debug("Get Request to /epgs/"+ userName + "?play=" + play + "&like=" + likeCurrentlyPlay + "&askedMovieNum=" + askedMovieNum);

		return getEpgRecommendationForUser(userName);
	}

	@GET
	@Path("epg/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Epg recommand(@PathParam("id") String userName) {

		List<Epg> moviesToRecommand = getEpgRecommendationForUser(userName);
		return moviesToRecommand.get(0);
	}

	private List<Epg> getEpgRecommendationForUser(String userName) {
		if (logger.isDebugEnabled()){
			stopWatch.reset();
			stopWatch.start();
		}

		Optional<ObjectId> currentlyPlayOption = Optional.ofNullable(play).filter(ObjectId::isValid).map(ObjectId::new);

		User user = epgService.getUser(userName);

		Authentication authentication = new TestingAuthenticationToken(user, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Optional<ObjectId> currentlyWatchOption = Optional.ofNullable(user.getCurrentlyWatch()).map(CurrentlyWatch::getMovieId);

		List<Movie> movies = epgService.getEpgRecommendationForUser(user, currentlyPlayOption, likeCurrentlyPlay, askedMovieNum);

		// TODO change to @async
		logger.debug("RestImpl, getEpgRecommendationForUser: " + Thread.currentThread().getName());
		epgService.doBackgroundJob(user, currentlyWatchOption, currentlyPlayOption, likeCurrentlyPlay);

		List<Epg> epgs = movies.stream().map(intooiTVMockService::fillMovieData).map(x -> intooiTVMockService.changeYouTubeUri(x)).map(Epg::new).collect(Collectors.toList());

		logger.debug("Response: " + epgs);

		if (logger.isDebugEnabled()){
			stopWatch.stop();
			logger.debug("Response time: " + stopWatch.getTime() + " miilis");
		}

		return epgs;
	}

}