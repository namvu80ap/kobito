package com.org.kobito.integration.services;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.repository.TradeTweetRepository;
import com.org.kobito.integration.repository.TweetTraderProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by v_nam on 2017/04/26.
 */
@Service
public class TradeTwetService {
    @Autowired
    private Twitter twitter;

    @Autowired
    private TradeTweetRepository tradeTweetRepository;

    @Autowired
    private TweetTraderProfileRepository tweetTraderProfileRepository;

    public Flux<TradeTweet>  searchByCreatedAt(){
        LocalDate localDate = LocalDate.now().plusDays(-1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Flux<TradeTweet> list = tradeTweetRepository.findByCreatedAtAfter(date);
        return list;
    }

}
