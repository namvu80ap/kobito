package com.org.kobito.integration.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        run();
//        if (processingEnabled) {
//            for (int i = 0; i < taskExecutor.getMaxPoolSize(); i++) {
//                taskExecutor.execute(new TweetProcessor(graphService, queue));
//            }
//            run();
//        }
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