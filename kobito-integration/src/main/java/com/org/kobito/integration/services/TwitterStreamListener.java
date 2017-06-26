package com.org.kobito.integration.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by v_nam on 2017/04/24.
 */
@Service
public class TwitterStreamListener implements StreamListener {

    private static final Logger logger = LogManager.getLogger(TwitterStreamListener.class);

    @Inject
    private Twitter twitter;

    private List<StreamListener> listeners = new ArrayList<>();

    @Autowired
    private  TradeTweetService tradeTweetService;

    public void run() {
        listeners.add(this);
        FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
        twitter.streamingOperations().filter( "#Forex", listeners);
        twitter.streamingOperations().filter( "#FX", listeners);
    }

    public void stop() {
        FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
        twitter.streamingOperations().filter ( "#Forex", null);
        twitter.streamingOperations().filter( "#FX", null);
    }

    @Override
    public void onTweet(Tweet tweet) {
        logger.debug("FOREX NEWS : {} , USER : {}" , tweet.getText() , tweet.getUser().getId() );
        tradeTweetService.saveTweet(tweet);
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