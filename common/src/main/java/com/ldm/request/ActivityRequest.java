package com.ldm.request;

import lombok.Data;

@Data
public class ActivityRequest {
    private String activityName;
    private String userId;
    private String activityType;
    private String locationName;
    private String publishTime;
}

