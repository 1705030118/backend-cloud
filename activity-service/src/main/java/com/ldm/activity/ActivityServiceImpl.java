package com.ldm.activity;

import com.ldm.api.ActivityService;
import com.ldm.request.ActivityRequest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ActivityServiceImpl implements ActivityService {
    @Override
    public void publish(ActivityRequest activityRequest) {

    }
}
