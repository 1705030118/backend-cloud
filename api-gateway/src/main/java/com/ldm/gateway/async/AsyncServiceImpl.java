package com.ldm.gateway.async;

import com.ldm.api.ActivityService;
import com.ldm.api.SearchService;
import com.ldm.domain.LogDomain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    public void createLog(LogDomain log) {
        searchService.createLog(log);
    }

    @Override
    @Async("asyncServiceExecutor")
    public void updateLog(LogDomain log) {
        searchService.updateLog(log);
    }
}
