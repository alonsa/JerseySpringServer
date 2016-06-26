package com.alon.main.server.rest;

import com.alon.main.server.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/recommend/")
@Component
public class RestImpl {

	//	http://localhost:8090/recommend/test

	@Autowired
	public RecommenderService recommenderService;

	@GET
	@Path("/test")
	public Response getCombinedMsg() {
		return Response.status(200).entity("zip").build();
	}
}