package com.ldm.activity;

import lombok.Data;

@Data
public class Activity {
    private int activityId;
    private String activityName;
    private int userId;
    private String activityType;
    private String locationName;
    private String publishTime;
}
