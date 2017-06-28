package com.org.kobito.integration.controller;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.services.ImportTweetHistory;
import com.org.kobito.integration.services.TwitterStreamListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by namvu on 17/04/24.
 */
@RestController
public class TwitterIntegrationController {

    private final Twitter twitter;

    @Autowired
    ImportTweetHistory importTweetHistory;

    @Autowired
    TwitterStreamListener twitterStreamListener;

    @Inject
    public TwitterIntegrationController(Twitter twitter) {
        this.twitter = twitter;
    }


    @GetMapping(value = "/importTweetHistory/{tweeterAccount}")
    public String importTweetHistory( @PathVariable String tweeterAccount ) {

        //Start import thread
        Runnable importThread = () -> { importTweetHistory.importTweet(tweeterAccount); };
        new Thread(importThread).start();

        return "Success";
    }

    @GetMapping(value = "/startTweetListener")
    public String startTweetListener( ) {

        //Start import thread
        Runnable importThread = () -> { twitterStreamListener.run(); };
        new Thread(importThread).start();

        return "Success";
    }

}