package com.ldm.api;

import com.ldm.domain.ActivityDomain;
import com.ldm.request.ActivityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "activity-service")
public interface ActivityService {
    /**
     * 发布活动
     * @param activityRequest
     */
    @PostMapping
    void publish(ActivityRequest activityRequest);
    @GetMapping
    List<ActivityDomain> selectAll();
}
