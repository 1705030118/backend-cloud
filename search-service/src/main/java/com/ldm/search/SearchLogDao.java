package com.ldm.search;

import com.ldm.domain.LogDomain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchLogDao  extends ElasticsearchRepository<SearchLogDomain,Integer> {
}
