package com.org.kobito.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cql.core.Ordering;
import org.springframework.data.cql.core.PrimaryKeyType;


import java.util.Date;
import java.util.UUID;

/**
 * Created by v_nam on 2017/04/25.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class TradeTweet {
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    private String tweetSource;
    private String text;
    private Float realTradingEval;

    @PrimaryKeyColumn(name = "createdat", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date createdAt;

    private String fromUser;
    private String profileImageUrl;
    private Long toUserId;
    private Long inReplyToStatusId;
    private Long inReplyToUserId;
    private String inReplyToScreenName;
    private long fromUserId;
    private String languageCode;
    private String source;
    private Integer retweetCount;
    private boolean retweeted;
    private boolean favorited;
    private Integer favoriteCount;

    @PrimaryKeyColumn(name = "profileid", ordinal = 1, type = PrimaryKeyType.PARTITIONED )
    private long profileId;

}
