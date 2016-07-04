package com.alon.main.server.rest;

import com.alon.main.server.dao.Dao;
import com.alon.main.server.entities.Movie;
import com.alon.main.server.entities.User;
import com.alon.main.server.service.RecommenderService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/recommend/")
@Component
public class RestImpl {


	//	http://localhost:8090/recommend/test

	@Autowired
	public RecommenderService recommenderService;

	@Autowired
	public Dao<Movie> movieDao;

	@Autowired
	public Dao<User> userDao;

	@GET
	@Path("{id}")
	public List<Movie> getCombinedMsg(@PathParam("id") String id) {

		Integer userInnerId = null;

//		User user;
//		if (ObjectId.isValid(id)){
//			user = userDao.getById(new ObjectId(id));
//			if (user != null){
//				userInnerId = user.getInnerId();
//			}
//		}

//		List<Integer> moviesInnerIds = userInnerId == null ?
//				recommenderService.recommend():
//				recommenderService.recommend(userInnerId);
//

		List<Integer> moviesInnerIds = recommenderService.recommend(Integer.valueOf(id));

		List<Movie> movies = movieDao.getByInnerIds(moviesInnerIds);

		return movies;
	}
}