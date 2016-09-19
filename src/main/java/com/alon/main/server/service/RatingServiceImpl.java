package com.alon.main.server.service;

import com.alon.main.server.dao.rating.RatingDao;
import com.alon.main.server.dao.rating.RatingMorphiaDaoImpl;
import com.alon.main.server.entities.Rating;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alon_ss on 6/26/16.
 */

@Service
public class RatingServiceImpl implements RatingService{

    private final static Logger logger = Logger.getLogger(RatingServiceImpl.class);

    @PostConstruct
    private void init() {
        logger.debug("################################");
        logger.debug("###   RatingService is up!   ###");
        logger.debug("################################");
    }

    @Autowired
    public RatingDao ratingDao;

    @Autowired
    public MovieService movieService;

    public List<Rating> getAllToList() {
        return ratingDao.getAllToList();
    }

    public void addRatings(List<Rating> newRatings) {
        List<Rating> checkedRatings = newRatings.stream().filter(Rating::isValid).collect(Collectors.toList());
        List<Rating> notValidRatings = newRatings.stream().filter(x-> !x.isValid()).collect(Collectors.toList());

        if (!notValidRatings.isEmpty()){
            logger.warn("There are some non valid rating that send to DB: " + notValidRatings);
        }

        ratingDao.saveAll(checkedRatings);
    }
}