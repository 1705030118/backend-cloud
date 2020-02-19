package com.ldm.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchDao extends ElasticsearchRepository<SearchDomain,Integer> {

}
