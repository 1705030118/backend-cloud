package com.ldm.api;

import com.github.pagehelper.PageInfo;
import com.ldm.domain.LogDomain;
import com.ldm.domain.SearchActivityDomain;

import java.util.Optional;

public interface SearchService {
    /**
     * 搜索活动
     * @param pageNum
     * @param pageSize
     * @param key
     * @return
     */
    PageInfo<SearchActivityDomain> search(int pageNum, int pageSize, String key);
    void save(SearchActivityDomain searchActivityDomain);
    void delete(SearchActivityDomain searchActivityDomain);
    Optional<SearchActivityDomain> findById(int activityId);
    /**
     * 新增系统日志
     */
    void createLog(LogDomain log);
    /**
     * 更新系统日志
     */
    void updateLog(LogDomain log);
}
