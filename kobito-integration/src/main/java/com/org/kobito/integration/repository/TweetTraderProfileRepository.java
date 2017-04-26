package com.org.kobito.integration.repository;

import com.org.kobito.integration.model.TweetTraderProfile;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by v_nam on 2017/04/26.
 */
@Repository
public interface TweetTraderProfileRepository extends CassandraRepository<TweetTraderProfile> {
}
