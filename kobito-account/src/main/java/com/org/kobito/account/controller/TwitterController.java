package com.org.kobito.account.controller;

import com.org.kobito.account.model.Account;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        SearchResults results = twitter.searchOperations().search("#spring" );
        List<Tweet> listTweet = results.getTweets();

        return listTweet;
    }

}