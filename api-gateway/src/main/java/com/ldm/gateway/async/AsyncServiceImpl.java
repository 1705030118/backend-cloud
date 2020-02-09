package com.ldm.gateway.async;

import com.ldm.api.ActivityService;
import com.ldm.api.SearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
public class AsyncServiceImpl implements AsyncService {

    private static Logger logger = LogManager.getLogger(AsyncServiceImpl.class.getName());
    @Autowired
    private SearchService searchService;
    @Autowired
    private ActivityService activityService;
    @Override
    @Async("asyncServiceExecutor")
    public void mysqlToEs() {

    }

    @Override
    @Async("asyncServiceExecutor")
    public void createLog(Log log) {

    }

    @Override
    @Async("asyncServiceExecutor")
    public void updateLog(Log log) {

    }
}
