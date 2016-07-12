package com.alon.main.server.rest;

import com.alon.main.server.entities.CurrentlyWatch;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import com.alon.main.server.service.MovieService;
import com.alon.main.server.service.RatingService;
import com.alon.main.server.service.RecommenderService;
import com.alon.main.server.service.UserService;
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

	@Autowired
	public RecommenderService recommenderService;

	@Autowired
	public UserService userService;

	@Autowired
	public RatingService ratingService;

	@Autowired
	public MovieService movieService;

	@DefaultValue(RESPONSE_NUM) @QueryParam("num") Integer recommandNum;
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

		// Get or create user
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

		// Calculate number of asked recommendations
		Integer recommendationNumber = user.getRecentlyWatch().size() + recommandNum;

		// Get recommended vods Ids
		List<Integer> recommendedMoviesInnerIds = recommenderService.recommend(user.getInnerId(), recommendationNumber);

		// Get the movies by the Ids
		List<Movie> dbMovies = movieService.getByInnerIds(recommendedMoviesInnerIds);

		// Create a Map of ides to movies
		Map<Integer, Movie> innerIdToMovie = dbMovies.stream().collect(Collectors.toMap(Movie::getInnerId, Function.identity()));

		// Map the movie ids to movies - while keep the order
		List<Movie> recommendedMovies = recommendedMoviesInnerIds.stream().map(innerIdToMovie::get).collect(Collectors.toList());

		HashSet<ObjectId> userRecentlyWatch = new HashSet<>(user.getRecentlyWatch());

		// Filter out the movies from the user recently watch list
		List<Movie> filteredMovie = recommendedMovies.stream()
				.filter(movie -> !userRecentlyWatch.contains(movie.getId()))
				.collect(Collectors.toList());

		// Add next play movie to the head of returned movies
		if (like){
			optionalPlayMovie.ifPresent(movie -> {
				filteredMovie.remove(movie);
				filteredMovie.add(0, movie);
			});
		}

		// Convert movies to Epg
		List<Epg> epgs = filteredMovie.stream().limit(recommandNum).map(Epg::new).collect(Collectors.toList());

		Optional<Movie> firstMovie = filteredMovie.stream().findFirst();
		Long epgLength = epgs.stream().findFirst().map(Epg::getLength).orElse(null);

		addMovieLengthFromEpg(firstMovie, epgLength);


		user.setCurrentlyWatch(firstMovie.map(CurrentlyWatch::new).orElse(null));

		userService.save(user); // TODO: need to update by field

		logger.debug("Response: " + epgs);

//		recommenderService.setModelAsync();

		return epgs;
	}

	@Deprecated
	private void addMovieLengthFromEpg(Optional<Movie> firstMovie, Long epgLength) {
		firstMovie.ifPresent(movie -> movie.setLength(epgLength));
	}


}