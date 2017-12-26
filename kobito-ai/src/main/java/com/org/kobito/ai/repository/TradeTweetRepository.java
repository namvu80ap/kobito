package com.org.kobito.ai.repository;

import com.org.kobito.ai.model.TradeTweet;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;

/**
 * Created by v_nam on 2017/01/26.
 */
@Repository
public interface TradeTweetRepository extends ReactiveCrudRepository<TradeTweet, String> {
//    Flux<TradeTweet> findByLastname(String lastname);
//    @Async
//    @Query("Select * from tradetweet where createdat>?0 ALLOW FILTERING ")
//    Flux<TradeTweet> findByCreatedAtAfter(Date date);
}
