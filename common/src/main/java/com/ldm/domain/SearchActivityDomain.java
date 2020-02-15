package com.ldm.domain;

import lombok.Data;

@Data
public class SearchActivityDomain {
    private Integer activityId;
    private Integer userId;
    private String activityName;
    private String activityType;
    private String locationName;
    private String publishTime;
}
