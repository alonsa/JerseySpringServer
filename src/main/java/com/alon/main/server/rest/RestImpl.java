package com.alon.main.server.rest;

import com.alon.main.server.dao.MorphiaBaseDao;
import com.alon.main.server.entities.BaseEntity;
import com.alon.main.server.entities.CurrentlyWatch;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import com.alon.main.server.movieProvider.YouTubeClient;
import com.alon.main.server.service.MovieService;
import com.alon.main.server.service.RatingService;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	public RatingService ratingService;

	@Autowired
	public MovieService movieService;

	@QueryParam("num") Integer recommandNum;
	@QueryParam("play") String play;
	@DefaultValue("true") @QueryParam("like") boolean like;

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

		Optional<Movie> optionalPlayMovie = Optional.empty();
		if (play != null && ObjectId.isValid(play)){
			Movie movie = movieService.getById(new ObjectId(play));
			optionalPlayMovie = Optional.ofNullable(movie);
		}

		// Add rating by the time the user watched the asset
		ratingService.addRating(user);

		// add rating by the user action (want to play or unlike the asset)
		optionalPlayMovie.ifPresent(movie -> ratingService.addRating(user.getInnerId(), movie.getInnerId(), like));

		if (recommandNum == null){
			recommandNum = RESPONSE_NUM;
		}

		logger.debug("Ask for recommendation for " +
				  "userId : " + userName +
				", user: " + user +
				", recommandNum: " + recommandNum +
				", play: "+ play);

//		List<Integer> moviesInnerIds = userInnerId == null ?
//				recommenderService.recommend():
//				recommenderService.recommend(userInnerId);

		Integer recommendationNumber = user.getRecentlyWatch().size() + recommandNum;

		List<Integer> recomandedMoviesInnerIds = recommenderService.recommend(user.getInnerId(), recommendationNumber);

		List<Movie> dbMovies = movieService.getByInnerIds(recomandedMoviesInnerIds);
		Map<Integer, Movie> innerIdToMovie = dbMovies.stream().collect(Collectors.toMap(Movie::getInnerId, Function.identity()));
		List<Movie> recommendedMovies = recomandedMoviesInnerIds.stream().map(innerIdToMovie::get).collect(Collectors.toList());

		if (like){
			optionalPlayMovie.ifPresent(movie -> recommendedMovies.add(0, movie));
		}

		HashSet<ObjectId> userRecentlyWatch = new HashSet<>(user.getRecentlyWatch());

		List<Movie> filteredMovie = recommendedMovies.stream().
				filter(movie -> !userRecentlyWatch.contains(movie.getId())).
				limit(recommandNum).collect(Collectors.toList());

		List<Epg> epgs = filteredMovie.stream().map(Epg::new).collect(Collectors.toList());

		Optional<Movie> firstMovie = filteredMovie.stream().findFirst();
		Long epgLength = epgs.stream().findFirst().map(Epg::getLength).orElse(null);

		firstMovie.map(movie -> {
			movie.setLength(epgLength);
			return movie;
		});

		user.setCurrentlyWatch(firstMovie.map(CurrentlyWatch::new).orElse(null));
		firstMovie.ifPresent(movie -> user.addToRecentlyWatch(movie.getId()));

		userService.save(user);

		logger.debug("Response: " + epgs);

//		recommenderService.setModelAsync();


		return epgs;
	}




}