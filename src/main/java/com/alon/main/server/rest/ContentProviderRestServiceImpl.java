package com.alon.main.server.rest;

import com.alon.main.server.service.ContentProviderService;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/content/")
@Component
public class ContentProviderRestServiceImpl {

//		http://localhost:8090/content?youTubeId=UCbcovN-W9s9bkoe0wqIEjGg

	private final static Logger logger = Logger.getLogger(ContentProviderRestServiceImpl.class);

	private static StopWatch stopWatch = new StopWatch();


	private ContentProviderService contentProviderService;

	@QueryParam("youTubeId") String youTubeId;
	@QueryParam("force") boolean force;

	@Autowired
	public ContentProviderRestServiceImpl(ContentProviderService contentProviderService) {

		this.contentProviderService = contentProviderService;

		logger.debug("################################################");
		logger.debug("###   ContentProviderRestServiceImpl is up!  ###");
		logger.debug("################################################");
	}

	@GET
	public Response addContentProviderData() {

		stopWatch.reset();
		stopWatch.start();
		ContentProviderData contentProviderData = contentProviderService.parseYoutubeContentProvider(youTubeId, force);

		StringBuilder sb = new StringBuilder();

		if (contentProviderData == null){
			sb.append("No content provider for youTubeId: ").append(youTubeId);
		}else {
			sb.append("Get content provider data for youTubeId: ").append(youTubeId).append("\n.");
			if (contentProviderData.isNew()){
				sb.append("A new ");
			}

			sb.append("content provider with name : ").append(contentProviderData.getName()).append("\n");
			sb.append("parsed : ").append(contentProviderData.getVodNumber()).append(" new vods").append("\n");
		}

		sb.append("The operation toke : ").append(stopWatch.getTime()/1000).append(" seconds").append("\n");
		stopWatch.reset();

		return Response.ok(sb.toString()).build();
	}


}