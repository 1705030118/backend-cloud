package com.ldm.api;

import com.github.pagehelper.PageInfo;
import com.ldm.domain.SearchActivityDomain;

public interface SearchService {
    /**
     * 搜索活动
     * @param pageNum
     * @param pageSize
     * @param key
     * @return
     */
    PageInfo<SearchActivityDomain> search(int pageNum, int pageSize, String key);
}
