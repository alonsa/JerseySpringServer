package com.alon.main.server.rest;

import com.alon.main.server.entities.*;
import com.alon.main.server.service.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alon.main.server.Const.Consts.RESPONSE_NUM;

@Path("/recommend/")
@Component
public class RestImpl {

//		http://localhost:8090/recommend/epg/9021
//		http://localhost:8090/recommend/epgs/9021

	private final static Logger logger = Logger.getLogger(RestImpl.class);

	private final RecommenderService recommenderService;

	private final UserService userService;

	private final RatingService ratingService;

	private final MovieService movieService;

	private final IntooiTVMockService intooiTVMockService;

	@DefaultValue(RESPONSE_NUM) @QueryParam("num") Integer recommandNum;
	@QueryParam("play") String play;
	@DefaultValue("true") @QueryParam("like") boolean like;

	@Autowired
	public RestImpl(RecommenderService recommenderService,
					UserService userService,
					MovieService movieService,
					RatingService ratingService,
					IntooiTVMockService intooiTVMockService) {

		this.recommenderService = recommenderService;
		this.userService = userService;
		this.movieService = movieService;
		this.ratingService = ratingService;
		this.intooiTVMockService = intooiTVMockService;
	}

	@GET
	@Path("epgs/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public List<Epg> recommands(@PathParam("id") String userName) {
		logger.debug("Get Request to /epgs/"+ userName + "?play=" + play + "&like=" + like + "&num=" + recommandNum);

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

		// Get or create user
		logger.debug("Look for user: " + userName);
		User user = userService.getUserByName(userName);
		logger.debug("Found user:" + user);

		Optional<Movie> optionalPlayMovie = Optional.empty();
		if (play != null && ObjectId.isValid(play)){
			Movie movie = movieService.getById(new ObjectId(play));
			optionalPlayMovie = Optional.ofNullable(movie);
		}

		logger.debug("Playing movie:" + optionalPlayMovie);

		// Add rating by the time the user watched the asset
		logger.debug("Add rating by the time the user watched the asset");
		ratingService.addRating(user);

		// Add rating by the user action (want to play or unlike the asset)
		logger.debug("Add rating by the user action (want to play or unlike the asset)");
		optionalPlayMovie.ifPresent(movie -> {
			Rating rating = ratingService.addRating(user.getInnerId(), movie.getInnerId(), like);
			org.apache.spark.mllib.recommendation.Rating recommendationRating =
					new org.apache.spark.mllib.recommendation.Rating(rating.getUserId(), rating.getMovieId(), rating.getRating());
			recommenderService.updateModel(recommendationRating);
		});

		// Calculate number of asked recommendations
		Integer recommendationNumber = user.getRecentlyWatch().size() + recommandNum;
		logger.debug("Number of asked recommendations: " +  recommendationNumber);


		// Get recommended vods Ids
		List<Integer> recommendedMoviesInnerIds = recommenderService.recommend(user.getInnerId(), recommendationNumber);
		logger.debug("Recommended vods Ids: " +  recommendedMoviesInnerIds);

		// Get the movies by the Ids
		List<Movie> dbMovies = movieService.getByInnerIds(recommendedMoviesInnerIds);

		// Create a Map of ides to movies
		Map<Integer, Movie> innerIdToMovie = dbMovies.stream().collect(Collectors.toMap(Movie::getInnerId, Function.identity()));

		// Map the movie ids to movies - while keep the order
		List<Movie> recommendedMovies = recommendedMoviesInnerIds.stream().map(innerIdToMovie::get).collect(Collectors.toList());

		HashSet<ObjectId> userRecentlyWatch = new HashSet<>(user.getRecentlyWatch());

		// Filter out the movies from the user recently watch list
		List<Movie> filteredMovie = recommendedMovies.stream().
				filter(movie -> !userRecentlyWatch.contains(movie.getId())).
				limit(recommandNum).
				collect(Collectors.toList());

		// Add next play movie to the head of returned movies
		optionalPlayMovie.ifPresent(filteredMovie::remove);
		if (like){
			optionalPlayMovie.ifPresent(movie -> filteredMovie.add(0, movie));
		}

		// Convert movies to Epg
		List<Epg> epgs = filteredMovie.stream().
				limit(recommandNum).
				map(intooiTVMockService::fillMovieData).
				map(Epg::new).
				collect(Collectors.toList());

		Optional<Movie> firstMovie = filteredMovie.stream().findFirst();

		user.setCurrentlyWatch(firstMovie.map(BaseEntity::getId).map(CurrentlyWatch::new).orElse(null));

		userService.save(user); // TODO: need to update by field
		logger.debug("Update user: " +  user);

		logger.debug("Response: " + epgs);

		return epgs;
	}

}