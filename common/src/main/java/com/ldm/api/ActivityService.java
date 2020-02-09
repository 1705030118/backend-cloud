package com.ldm.api;

import com.ldm.request.ActivityRequest;

public interface ActivityService {
    /**
     * 发布活动
     * @param activityRequest
     */
    void publish(ActivityRequest activityRequest);
}
