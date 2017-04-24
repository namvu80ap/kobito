package com.org.kobito.integration.controller;

import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by namvu on 17/04/24.
 */
@RestController
public class TwitterController {

    private final Twitter twitter;


    @Inject
    public TwitterController(Twitter twitter) {
        this.twitter = twitter;
    }

    @GetMapping("/search")
    List<Tweet> search() {
//        SearchResults results = twitter.searchOperations().search("#spring" );
//        List<Tweet> listTweet = results.getTweets();
        long senceId =  856413158214438912L;
        long maxId = 856402327762345985L;
        List<Tweet> tweets = null;
        try {
            tweets = twitter.timelineOperations().getUserTimeline("SignalFactory", 2,maxId,senceId);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return tweets;
    }

}