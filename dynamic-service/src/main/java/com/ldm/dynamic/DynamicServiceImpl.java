package com.ldm.dynamic;

import com.ldm.api.DynamicService;
import com.ldm.request.DynamicRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicServiceImpl implements DynamicService {
    @Autowired
    DynamicDao dynamicDao;
    @Override
    public boolean publish(DynamicRequest dynamicRequest) {
        return dynamicDao.publish(dynamicRequest)>0;
    }
}
