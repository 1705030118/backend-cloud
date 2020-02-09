package com.ldm.activity;

import com.ldm.request.ActivityRequest;

public interface ActivityDao {
    // 发布活动
    int publish(ActivityRequest activityRequest);
}
