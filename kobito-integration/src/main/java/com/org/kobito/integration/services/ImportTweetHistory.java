package com.org.kobito.integration.services;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.model.TweetTraderProfile;
import com.org.kobito.integration.repository.TradeTweetRepository;
import com.org.kobito.integration.repository.TweetTraderProfileRepository;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Value("${kobito.config.twitter.importTweetPageSize}")
    private Integer importTweetPageSize;

    @Autowired
    private Twitter twitter;

    @Autowired
    private TradeTweetService tradeTweetService;

    @Autowired
    private TradeTweetRepository tradeTweetRepository;

    @Autowired
    private TweetTraderProfileRepository tweetTraderProfileRepository;

    public void importTweet( @NonNull String tweetAccount ) {
        logger.debug("TwitterTrader : {} ", tweetAccount);
        List<Tweet> list = twitter.timelineOperations().getUserTimeline(tweetAccount,importTweetPageSize);
        if(list != null)
            saveTweet(list);
        if(list != null && list.size() == importTweetPageSize){
            Tweet lastItem = list.get(importTweetPageSize-1);
            while(lastItem!=null){
                List<Tweet> nextList = twitter.timelineOperations()
                                              .getUserTimeline(tweetAccount, importTweetPageSize,
                                                                1, new Long(lastItem.getId()));
                saveTweet(nextList);
                if(nextList == null || nextList.size() < importTweetPageSize){
                    break;
                }
                lastItem = nextList.get(importTweetPageSize-1);
            }
        }
    }

    public void getTraderTweetProfile(){
        for ( TweetTraderProfile profile : tweetTraderProfileRepository.findAll()) {
            logger.info( "Import Tweet of : {} ", profile.getScreenName() );
            this.importTweet( profile.getScreenName() );
        }
    }

    public void saveTweet( List<Tweet> items ){
        items.parallelStream().forEach(
            item -> {
                        logger.info("Save tweet: Id {} , Text {}, Name {} " , item.getId(),
                                    item.getText(), item.getUser().getName() );
                        tradeTweetService.saveTweet(item);
                    }
        );
    }




}
