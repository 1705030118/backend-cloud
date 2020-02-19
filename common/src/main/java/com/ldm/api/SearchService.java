package com.ldm.api;

import com.github.pagehelper.PageInfo;
import com.ldm.domain.LogDomain;
import com.ldm.domain.SearchActivityDomain;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@FeignClient(value = "search-service")
public interface SearchService {
    /**
     * 搜索活动
     * @param pageNum
     * @param pageSize
     * @param key
     * @return
     */
    @GetMapping("/{pageNum}/{pageSize}/{key}")
    PageInfo<SearchActivityDomain> search(@PathVariable("pageNum") int pageNum,
                                          @PathVariable("pageSize") int pageSize,@PathVariable("key") String key);
    @PostMapping
    void save(SearchActivityDomain searchActivityDomain);
    @DeleteMapping
    void deleteActivity(SearchActivityDomain searchActivityDomain);
    /**
     * 新增系统日志
     */
    @PostMapping
    void createLog(LogDomain log);
    /**
     * 更新系统日志
     */
    @PutMapping
    void updateLog(LogDomain log);
}
