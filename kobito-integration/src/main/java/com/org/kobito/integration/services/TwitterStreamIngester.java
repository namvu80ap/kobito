package com.org.kobito.integration.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by v_nam on 2017/04/24.
 */
@Service
public class TwitterStreamIngester implements StreamListener {

    private static final Logger logger = LogManager.getLogger(TwitterStreamIngester.class);

    @Inject
    private Twitter twitter;

    public void run() {
        List<StreamListener> listeners = new ArrayList<>();
        listeners.add(this);
        logger.info("TWITTER SERIVEC RUN");
        logger.info("TWITTER SERIVEC RUN");
        logger.info("TWITTER SERIVEC RUN");
        logger.info("TWITTER SERIVEC RUN");logger.info("TWITTER SERIVEC RUN");

        twitter.streamingOperations().filter( "#FX", listeners);
        twitter.streamingOperations().filter( "#Forex", listeners);
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
//        run();
    }

    @Override
    public void onTweet(Tweet tweet) {
        logger.info("NEW TWITTER : {} , USER : {}" , tweet.getText() , tweet.getFromUser() );
    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {
    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {
    }
}