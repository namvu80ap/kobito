package com.org.kobito.integration.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kobito.config.twitter.tradingWords}")
    private String[] tradingWords;

    @Autowired
    private  ImportTweetHistory importTweetHistory;

    public void run() {
        List<StreamListener> listeners = new ArrayList<>();
        listeners.add(this);
        FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
//        filterStreamParameters.follow(54227657);

        filterStreamParameters.follow(14710799);
//        twitter.streamingOperations().filter( filterStreamParameters , listeners);
        twitter.streamingOperations().filter( "#Forex", listeners);
        twitter.streamingOperations().filter( "#FX", listeners);

//        twitter.streamingOperations().sample(  listeners);
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        run();
    }

    @Override
    public void onTweet(Tweet tweet) {
        logger.info("FOREX NEWS : {} , USER : {}" , tweet.getText() , tweet.getUser().getId() );
        if(tradingWords != null){
            if( Arrays.stream(tradingWords).parallel().anyMatch(tweet.getText()::contains ) ){
                logger.info("TRADE TWEET: {} , USER : {}" , tweet.getText() , tweet.getUser().getId() );
                importTweetHistory.saveTweet(tweet);
            }
        }
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