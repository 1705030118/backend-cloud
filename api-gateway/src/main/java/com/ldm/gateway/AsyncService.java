package com.ldm.gateway;

public interface AsyncService {
    /**
     *  执行异步任务
     *  将MySQL中活动数据表导入Elasticsearch中
     */
    void mysqlToES();
}
