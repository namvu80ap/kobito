package com.org.kobito.integration.services;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.model.TweetTraderProfile;
import com.org.kobito.integration.repository.TradeTweetRepository;
import com.org.kobito.integration.repository.TweetTraderProfileRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Created by v_nam on 2017/04/25.
 */
@Service
public class ImportTweetHistory {

    private static final Logger logger = LogManager.getLogger(ImportTweetHistory.class);

    @Value("${kobito.config.twitter.importTweetHistory}")
    private Boolean importTweetHistory;

    @Autowired
    private Twitter twitter;

    @Autowired
    private TradeTweetRepository tradeTweetRepository;

    @Value("${kobito.config.twitter.twitterTraders}")
    private String[] twitterTraders;

    @Autowired
    private TweetTraderProfileRepository tweetTraderProfileRepository;

    public void run() {
        logger.debug("TWITTER SERIVEC RUN");
        if(importTweetHistory && ArrayUtils.isNotEmpty(twitterTraders) ){
            Arrays.stream(twitterTraders).parallel().forEach(
                item -> {
                    logger.info("TwitterTrader : {} ", item);
                    item = "@" + item;
                    List<Tweet> list = twitter.timelineOperations().getUserTimeline(item.toString(),50);
                    if(list != null)
                        saveTweet(list);
                    if(list != null && list.size() == 50){
                        Tweet lastItem = list.get(49);
                        while(lastItem!=null){
                            List<Tweet> nextList = twitter.timelineOperations().getUserTimeline(item.toString(),50, 1, new Long(lastItem.getId()));
                            saveTweet(list);
                            if(nextList == null || nextList.size() < 50){
                                break;
                            }
                            lastItem = nextList.get(49);
                        }
                    }
                }
            );
        }
    }

    public void saveTweet( List<Tweet> items ){
//        tradeTweetRepository.save(
//                Flux.from(items).flatMap( i -> )
//        ).subcribe();

        items.parallelStream().forEach(
            item -> logger.info("Save tweet: {}", item.getText())
        );
    }

    public void saveTweet( Tweet item ){

        TweetTraderProfile tweetTraderProfile = new TweetTraderProfile();
        BeanUtils.copyProperties( item.getUser(), tweetTraderProfile );
        tweetTraderProfileRepository.save(tweetTraderProfile);

        TradeTweet tradeTweet = new TradeTweet();
        tradeTweet.setProfileId(tweetTraderProfile.getId());
        BeanUtils.copyProperties( item, tradeTweet );
        tradeTweetRepository.save(tradeTweet).subscribe();

    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
//        run();
    }
}
