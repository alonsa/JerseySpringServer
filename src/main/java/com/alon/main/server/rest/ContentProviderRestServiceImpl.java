package com.alon.main.server.rest;

import com.alon.main.server.service.ContentProviderService;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/content/")
@Component
public class ContentProviderRestServiceImpl {

//		http://localhost:8090/content?youTubeId=UCbcovN-W9s9bkoe0wqIEjGg

	private final static Logger logger = Logger.getLogger(ContentProviderRestServiceImpl.class);

	private ContentProviderService contentProviderService;

	private static StopWatch stopWatch = new StopWatch();

	@QueryParam("youTubeId") String youTubeId;

	@Autowired
	public ContentProviderRestServiceImpl(ContentProviderService contentProviderService) {

		this.contentProviderService = contentProviderService;

		logger.debug("################################################");
		logger.debug("###   ContentProviderRestServiceImpl is up!  ###");
		logger.debug("################################################");
	}

	@GET
	public Response addContentProviderData() {
		contentProviderService.parseYoutubeContentProvider(youTubeId);

		return Response.ok("aaaa").build();
	}


}