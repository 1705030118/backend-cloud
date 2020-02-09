package com.ldm.gateway;

import com.ldm.api.ActivityService;
import com.ldm.api.SearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public class SearchController implements InitializingBean,AsyncService {
    private static Logger logger = LogManager.getLogger(SearchController.class.getName());
    @Autowired
    private SearchService searchService;
    @Autowired
    private ActivityService activityService;
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    @Async("asyncServiceExecutor")
    public void mysqlToES() {
        logger.info("线程-" + Thread.currentThread().getId() + "在执行");

    }
}
