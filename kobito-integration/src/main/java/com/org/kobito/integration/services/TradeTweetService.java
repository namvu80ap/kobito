package com.org.kobito.integration.services;

import com.org.kobito.integration.model.CommonFxTweet;
import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.model.TweetTraderProfile;
import com.org.kobito.integration.repository.CommonFxTweetRepository;
import com.org.kobito.integration.repository.TradeTweetRepository;
import com.org.kobito.integration.repository.TweetTraderProfileRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;

/**
 * Created by v_nam on 2017/04/26.
 *
 */
@Service
public class TradeTweetService {

    private static final Logger logger = LogManager.getLogger(TradeTweetService.class);

    @Autowired
    private TradeTweetRepository tradeTweetRepository;
    @Autowired
    private CommonFxTweetRepository commonTweetRepository;
    @Autowired
    private TweetTraderProfileRepository tweetTraderProfileRepository;

    @Value("${kobito.config.twitter.tradingWords}")
    private String[] tradingWords;

    /**
     * Save and divide into TradeTweet or CommonFxTweet
     * @param tweet
     */
    public void saveTweet( Tweet tweet ) {
        if(tradingWords != null) {
            logger.info( "fx and forex tweet: {} , screenname : {} " , tweet.getId() , tweet.getUser().getScreenName());
            this.saveCommonTweet(tweet);
        }
    }

    /**
     * Save the tweet we sure that is tradetweet
     * @param item
     * @param realTradingEval
     */
    public void saveTradeTweet( Tweet item, Float realTradingEval ) {
        logger.debug("SAVE TWEET : {} ", item.toString());
        this.saveTradeProfile(item);
        TradeTweet tradeTweet = new TradeTweet();
        tradeTweet.setProfileId( item.getUser().getId());
        BeanUtils.copyProperties( item, tradeTweet );
        tradeTweet.setRealTradingEval(realTradingEval);
        tradeTweetRepository.save(tradeTweet).subscribe();
    }

    private void saveCommonTweet( Tweet item ) {
        logger.debug("SAVE TWEET : {} ", item.toString());
        this.saveTradeProfile(item);
        CommonFxTweet commonTweet = new CommonFxTweet();
        commonTweet.setProfileId( item.getUser().getId());
        BeanUtils.copyProperties( item, commonTweet );
        commonTweetRepository.save(commonTweet).subscribe();
    }

    public void saveTradeProfile( Tweet item  ){
        TweetTraderProfile tweetTraderProfile = new TweetTraderProfile();
        BeanUtils.copyProperties( item.getUser(), tweetTraderProfile );
        tweetTraderProfileRepository.save(tweetTraderProfile);
    }

    public boolean isSimiliarTradeTweet( String tweet ){

       return true;
    }
}
