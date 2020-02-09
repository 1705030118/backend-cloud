package com.ldm.gateway;

import com.ldm.api.CacheService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class UserController implements InitializingBean {
    @Autowired
    private CacheService cacheService;
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
