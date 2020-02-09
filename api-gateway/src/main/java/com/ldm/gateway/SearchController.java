package com.ldm.gateway;

import com.ldm.api.ActivityService;
import com.ldm.api.SearchService;
import com.ldm.gateway.async.AsyncService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchController implements InitializingBean {
    private static Logger logger = LogManager.getLogger(SearchController.class.getName());
    @Autowired
    private SearchService searchService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private AsyncService asyncService;

    /**
     * 初始化时，通过线程池把MySQL中的数据导入ES
     */
    @Override
    public void afterPropertiesSet() {
        //调用service层的任务
        asyncService.mysqlToEs();
        logger.info("end submit");
    }
}
