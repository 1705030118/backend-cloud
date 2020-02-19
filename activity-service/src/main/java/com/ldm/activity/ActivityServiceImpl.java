package com.ldm.activity;

import com.ldm.api.ActivityService;
import com.ldm.domain.ActivityDomain;
import com.ldm.request.ActivityRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RefreshScope
@RestController
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Override
    public void publish(ActivityRequest activityRequest) {

    }

    @Override
    public List<ActivityDomain> selectAll() {
        return activityDao.selectAll();
    }
    @Test
    public void test(){
        List<Activity> activityList=activityDao.getAll();
        System.out.println(activityList.size());
    }
}
