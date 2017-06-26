package com.org.kobito.integration.repository;

import com.org.kobito.integration.model.CommonTweet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by v_nam on 2017/06/27.
 */
@Repository
public interface CommonTweetRepository extends ReactiveCrudRepository<CommonTweet, String> {
}