package com.ldm.gateway.async;

import com.ldm.domain.LogDomain;

public interface AsyncService {
    /**
     *  执行异步任务
     *  将MySQL中活动数据表导入Elasticsearch中
     */
    void mysqlToEs();
    /**
     * 新增系统日志
     */
    void createLog(LogDomain log);
    /**
     * 更新系统日志
     */
    void updateLog(LogDomain log);
}
