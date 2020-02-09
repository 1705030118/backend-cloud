package com.ldm.search;

import com.ldm.domain.SearchActivityDomain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchDao extends ElasticsearchRepository<SearchActivityDomain,Integer> {

}
