package com.ldm.api;

import com.ldm.request.DynamicRequest;

public interface DynamicService {
    /**
     * 发布动态
     * @param dynamicRequest
     */
    boolean publish(DynamicRequest dynamicRequest);
    /**
     * 动态主页
     */


}
