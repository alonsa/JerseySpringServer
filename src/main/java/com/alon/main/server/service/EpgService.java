package com.alon.main.server.service;

import com.alon.main.server.entities.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by alon_ss on 6/26/16.
 */

public interface EpgService {

    Future<List<Rating>> doBackgroundJob(User user, Optional<ObjectId> currentlyWatchOptional,
                                         Optional<ObjectId> currentlyPlayOptional, Boolean isLikeCurrentlyPlay);

    User getUser(String userName);

    List<Movie> getEpgRecommendationForUser(User user, Optional<ObjectId> currentlyPlayOption,
                                            boolean likeCurrentlyPlay, Integer recommandNum);

}