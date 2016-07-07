package com.alon.main.server.rest;

import com.alon.main.server.dao.RecommandDao;
import com.alon.main.server.dao.counter.CounterMorphiaDaoImpl;
import com.alon.main.server.dao.user.UserMorphiaDaoImpl;
import com.alon.main.server.entities.ExternalId;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import com.alon.main.server.service.MovieService;
import com.alon.main.server.service.RecommenderService;
import com.alon.main.server.service.UserService;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.RECENTLY_WATCH_MAX_SIZE;
import static com.alon.main.server.Const.Consts.RESPONSE_NUM;

@Path("/recommend/")
@Component
public class RestImpl {


//		http://localhost:8090/recommend/epg/9021
//		http://localhost:8090/recommend/epgs/9021

	@Autowired
	public RecommenderService recommenderService;

	@Autowired
	public UserService userService;

	@Autowired
	public MovieService movieService;

	@GET
	@Path("epgs/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public List<EPG> recommands(@PathParam("id") String userName, @QueryParam("num") Integer recommandNum) {

		return getEpgRecommendationForUser(userName, recommandNum);
	}

	@GET
	@Path("epg/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EPG recommand(@PathParam("id") String userName, @QueryParam("num") Integer recommandNum) {

		List<EPG> moviesToRecommand = getEpgRecommendationForUser(userName, recommandNum);


		return moviesToRecommand.get(0);
	}

	private List<EPG> getEpgRecommendationForUser(String userName, @QueryParam("num") Integer recommandNum) {
		Integer userInnerId = null;

		User user = userService.getUserByName(userName);

		if (recommandNum == null){
			recommandNum = RESPONSE_NUM;
		}

//		List<Integer> moviesInnerIds = userInnerId == null ?
//				recommenderService.recommend():
//				recommenderService.recommend(userInnerId);

		Integer recommendationNumber = RECENTLY_WATCH_MAX_SIZE + recommandNum;

		List<Integer> moviesInnerIds = recommenderService.recommend(user.getInnerId(), recommendationNumber);

		List<Movie> movies = movieService.getByInnerIds(moviesInnerIds);

		HashSet<ObjectId> userRecentlyWatch = new HashSet<>(user.getRecentlyWatch());
		return movies.stream().
				filter(movie -> !userRecentlyWatch.contains(movie.getId())).
				limit(recommandNum).map(EPG::new).
				collect(Collectors.toList());
	}

}