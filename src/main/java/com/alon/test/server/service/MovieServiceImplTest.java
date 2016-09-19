package com.alon.test.server.service;

import com.alon.main.server.dao.movie.MovieMorphiaDaoImpl;
import com.alon.main.server.entities.*;
import com.alon.main.server.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bson.types.ObjectId;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by alon_ss on 7/31/16.
 */
public class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl testingObject;

    @Mock
    private MovieMorphiaDaoImpl movieMorphiaDao;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUser() throws Exception {

        testingObject.save(getMovie());
    }

    private Movie getMovie() {


        Movie movie = new Movie(1, "title", null, null);
        movie.setId(new ObjectId());

        return movie;
    }

}