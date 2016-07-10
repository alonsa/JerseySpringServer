package com.alon.main.server.rest;

import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import com.alon.main.server.movieProvider.YouTubeClient;
import com.alon.main.server.service.MovieService;
import com.alon.main.server.service.RecommenderService;
import com.alon.main.server.service.UserService;
import org.apache.log4j.Logger;
import org.apache.spark.sql.catalyst.expressions.aggregate.Final;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.RECENTLY_WATCH_MAX_SIZE;
import static com.alon.main.server.Const.Consts.RESPONSE_NUM;

@Path("/recommend/")
@Component
public class RestImpl {


//		http://localhost:8090/recommend/epg/9021
//		http://localhost:8090/recommend/epgs/9021


	private final static Logger logger = Logger.getLogger(RestImpl.class);


	@Autowired
	public RecommenderService recommenderService;

	@Autowired
	public UserService userService;

	@Autowired
	public MovieService movieService;

	@QueryParam("num") Integer recommandNum;
	@QueryParam("play") String play;

	@GET
	@Path("epgs/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public List<Epg> recommands(@PathParam("id") String userName) {

		return getEpgRecommendationForUser(userName);
	}

	@GET
	@Path("epg/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Epg recommand(@PathParam("id") String userName, @QueryParam("num") Integer recommandNum) {

		List<Epg> moviesToRecommand = getEpgRecommendationForUser(userName);


		return moviesToRecommand.get(0);
	}

	private List<Epg> getEpgRecommendationForUser(String userName) {


		User user = userService.getUserByName(userName);

		if (recommandNum == null){
			recommandNum = RESPONSE_NUM;
		}

//		recommenderService.

		logger.debug("Ask for recommendation for " +
				  "userId : " + userName +
				", user: " + user +
				", recommandNum: " + recommandNum +
				", play: "+ play);

//		List<Integer> moviesInnerIds = userInnerId == null ?
//				recommenderService.recommend():
//				recommenderService.recommend(userInnerId);

		recommenderService.setModelAsync();

		Integer recommendationNumber = RECENTLY_WATCH_MAX_SIZE + recommandNum;

		List<Integer> moviesInnerIds = recommenderService.recommend(user.getInnerId(), recommendationNumber);

		List<Movie> movies = movieService.getByInnerIds(moviesInnerIds);

		HashSet<ObjectId> userRecentlyWatch = new HashSet<>(user.getRecentlyWatch());

		List<Epg> epgs = movies.stream().
				filter(movie -> !userRecentlyWatch.contains(movie.getId())).
				limit(recommandNum).map(Epg::new).
				collect(Collectors.toList());

		logger.debug("Response: " + epgs);


		return epgs;
	}




}