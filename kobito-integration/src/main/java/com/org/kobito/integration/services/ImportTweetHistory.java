package com.org.kobito.integration.services;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.model.TweetTraderProfile;
import com.org.kobito.integration.repository.TradeTweetRepository;
import com.org.kobito.integration.repository.TweetTraderProfileRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;

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
        if(importTweetHistory){
//            SearchResults results = twitter.searchOperations().search("SignalFactory until:2017-04-10", 10);
            List<Tweet> list = twitter.timelineOperations().getUserTimeline("SignalFactory",50);
            if( list != null )
            list.parallelStream().forEach( item -> saveTweet(item));

            if( list != null && list.size() == 50 ){
                Tweet lastItem = list.get(49);
//                logger.debug("LAST TWEET : id {} , date : {} , text : {}", lastItem.getId(),lastItem.getCreatedAt() ,lastItem.getText() );
                while(lastItem!=null){
                    List<Tweet> nextList = twitter.timelineOperations().getUserTimeline("SignalFactory",50, 1, new Long( lastItem.getId() ) );
                    logger.debug("ALL 1000 TWEET : {}" , list.size() );
                    nextList.parallelStream().forEach( item ->  saveTweet(item) );
                    if(nextList == null || nextList.size() < 50){
                        break;
                    }
                    lastItem = nextList.get(49);
                }
            }
        }
    }

    public void saveTweet( Tweet item ){
        logger.debug("TWEET : id {} , date : {} , text : {}", item.getId(),item.getCreatedAt() ,item.getText() );
        TradeTweet tradeTweet = new TradeTweet();
        TweetTraderProfile tweetTraderProfile = new TweetTraderProfile();

        BeanUtils.copyProperties( item.getUser(), tweetTraderProfile );
        tweetTraderProfileRepository.save(tweetTraderProfile);

        tradeTweet.setProfileId(tweetTraderProfile.getId());
        BeanUtils.copyProperties( item, tradeTweet );
        tradeTweetRepository.save(tradeTweet).subscribe();

        logger.info("FINISH SAVE TWEET: {}" , item.getId() );
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        run();
    }
}
