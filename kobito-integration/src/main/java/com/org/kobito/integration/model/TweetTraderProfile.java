package com.org.kobito.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by v_nam on 2017/04/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class TweetTraderProfile implements Serializable {

    @PrimaryKey
    private long id;

    private String screenName;
    private String name;
    private String url;
    private String profileImageUrl;
    private String description;
    private String location;
    private Date createdDate;
    private String language;
    private int statusesCount;
    private int friendsCount;
    private int followersCount;
    private int favoritesCount;
    private int listedCount;
    private boolean following;
    private boolean followRequestSent;
    private boolean isProtected;
    private boolean notificationsEnabled;
    private boolean verified;
    private boolean geoEnabled;
    private boolean contributorsEnabled;
    private boolean translator;
    private String timeZone;
    private int utcOffset;
    private String sidebarBorderColor;
    private String sidebarFillColor;
    private String backgroundColor;
    private boolean useBackgroundImage;
    private String backgroundImageUrl;
    private boolean backgroundImageTiled;
    private String textColor;
    private String linkColor;
    private boolean showAllInlineMedia;
    private String profileBannerUrl;

}
