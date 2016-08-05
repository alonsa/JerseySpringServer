package com.alon.main.server.rest;

import com.alon.main.server.entities.*;
import com.alon.main.server.service.*;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.RESPONSE_NUM;

@Path("/recommend/")
@Component
public class RestImpl {

//		http://localhost:8090/recommend/epg/9021
//		http://localhost:8090/recommend/epgs/9021

	private final static Logger logger = Logger.getLogger(RestImpl.class);

	private final RestApiService restApiService;
	private final IntooiTVMockService intooiTVMockService;

	private static StopWatch stopWatch = new StopWatch();

	@DefaultValue(RESPONSE_NUM) @QueryParam("num") Integer askedMovieNum;
	@QueryParam("play") String play;
	@DefaultValue("true") @QueryParam("like") boolean likeCurrentlyPlay;

	@Autowired
	public RestImpl(RestApiService restApiService, IntooiTVMockService intooiTVMockService) {

		this.restApiService = restApiService;
		this.intooiTVMockService = intooiTVMockService;

		logger.debug("##########################");
		logger.debug("###   RestImpl is up!  ###");
		logger.debug("##########################");
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

		User user = restApiService.getUser(userName);
		Optional<ObjectId> currentlyWatchOption = Optional.ofNullable(user.getCurrentlyWatch()).map(CurrentlyWatch::getMovieId);

		List<Movie> movies = restApiService.getEpgRecommendationForUser(user, currentlyPlayOption, likeCurrentlyPlay, askedMovieNum);

		// TODO change to @async
		BackgroundJob backgroundJob = new BackgroundJob(user, currentlyWatchOption, currentlyPlayOption);
		Thread thread = new Thread(backgroundJob);
		thread.start();

		List<Epg> epgs = movies.stream().map(intooiTVMockService::fillMovieData).map(Epg::new).collect(Collectors.toList());

		logger.debug("Response: " + epgs);

		if (logger.isDebugEnabled()){
			stopWatch.stop();
			logger.debug("Response time: " + stopWatch.getTime() + " miilis");
		}

		return epgs;
	}

	private class BackgroundJob implements Runnable{
		private Optional<ObjectId> currentlyPlayOption;
		private User user;
		private Optional<ObjectId> currentlyWatchOption;

		BackgroundJob(User user, Optional<ObjectId> currentlyWatchOption, Optional<ObjectId> currentlyPlayOption){
			this.currentlyPlayOption = currentlyPlayOption;
			this.user = user;
			this.currentlyWatchOption = currentlyWatchOption;
		}

		@Override
		public void run() {
			StopWatch stopWatch = null;
			if (logger.isDebugEnabled()){
				stopWatch = new StopWatch();
				stopWatch.reset();
				stopWatch.start();
			}
			logger.debug("Start BackgroundJob");

			restApiService.doBackgroundJob(user, currentlyWatchOption, currentlyPlayOption, likeCurrentlyPlay);
			if (logger.isDebugEnabled() && stopWatch != null){
				stopWatch.stop();
				logger.debug("BackgroundJob run time: " + stopWatch.getTime() + " miilis");
			}
			logger.debug("End BackgroundJob");

		}
	}

}