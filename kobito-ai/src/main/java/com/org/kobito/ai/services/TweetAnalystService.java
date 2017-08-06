package com.org.kobito.ai.services;

import com.org.kobito.ai.model.TradeTweet;
import com.org.kobito.ai.repository.TradeTweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by v_nam on 2017/08/01.
 */
@Service
public class TweetAnalystService {
    @Autowired
    TradeTweetRepository tradeTweetRepository;

    /**
     * Analyst the tweet text, how similiar this text with trading tweet.
     * @param tweetStr
     * @return float value 0 to 0.99
     */
    public List<TradeTweet> analystIncomeTweet(String tweetStr ){
        Flux<TradeTweet> tweetFlux = tradeTweetRepository.findAll();
        Stream< List<TradeTweet> > stream = tweetFlux.buffer(200, 200).toStream();
//        stream.forEach( list -> {
//            for ( TradeTweet item : list ) {
//                System.out.println( item.getText() );
//            }
//        } );
        return  stream.findAny().get();
    }
}
