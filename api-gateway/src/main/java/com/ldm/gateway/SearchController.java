package com.ldm.gateway;

import com.ldm.api.ActivityService;
import com.ldm.api.SearchService;
import com.ldm.domain.ActivityDomain;
import com.ldm.gateway.async.AsyncService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search")
@RestController
public class SearchController implements InitializingBean {
    private static Logger logger = LogManager.getLogger(SearchController.class.getName());
    @Autowired
    private SearchService searchService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private AsyncService asyncService;
    @Test
    public void test(){
        logger.debug("searchController 初始化");
        List<ActivityDomain> activityDomainList=activityService.selectAll();
        logger.debug(activityDomainList.size());
        for (ActivityDomain activityDomain:activityDomainList){
            logger.info(activityDomain);
            asyncService.mysqlToEs(activityDomain.transform());
        }
    }

    /**
     * 初始化时，通过线程池把MySQL中的数据导入ES
     */
    @Override
    public void afterPropertiesSet() {
//        logger.debug("searchController 初始化");
//        List<ActivityDomain> activityDomainList=activityService.selectAll();
//        for (ActivityDomain activityDomain:activityDomainList){
//            asyncService.mysqlToEs(activityDomain.transform());
//        }

    }
}
