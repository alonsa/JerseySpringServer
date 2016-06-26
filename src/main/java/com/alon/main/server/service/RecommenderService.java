package com.alon.main.server.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by alon_ss on 6/26/16.
 */
@Service
public class RecommenderService
{
    @PostConstruct
    private void init() {
        System.out.println("RecommenderService PostConstruct");
    }

}