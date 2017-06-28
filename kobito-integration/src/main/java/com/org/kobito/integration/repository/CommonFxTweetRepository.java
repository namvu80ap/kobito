package com.org.kobito.integration.repository;

import com.org.kobito.integration.model.CommonFxTweet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by v_nam on 2017/06/27.
 */
@Repository
public interface CommonFxTweetRepository extends ReactiveCrudRepository<CommonFxTweet, String> {
}