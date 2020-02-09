package com.ldm.search;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ldm.api.SearchService;
import com.ldm.domain.SearchActivityDomain;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;
    @Autowired
    private EsConfig esConfig;
    @Override
    public PageInfo<SearchActivityDomain> search(int pageNum, int pageSize, String key) {
        System.out.println(pageNum+" "+pageSize+" "+key);
        PageHelper.startPage(pageNum, pageSize);
        Client client = esConfig.esTemplate();
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.multiMatchQuery(key,"bookName","author","press","bookType"));

        //搜索数据
        SearchResponse response = client.prepareSearch("mytest")
                .setQuery(boolQueryBuilder)
                .execute().actionGet();
        SearchHits searchHits = response.getHits();
        System.out.println(searchHits.getTotalHits());
        List<SearchDomain> list = new ArrayList<>();
        int ans=0;
        for(SearchHit hit : searchHits) {
            ans++;
            if(ans<=(pageNum-1)*pageSize) continue;
            if(ans>pageNum*pageSize) break;
            SearchDomain entity = new SearchDomain();
            Map<String, Object> entityMap = hit.getSourceAsMap();

            //map to object
            if(!CollectionUtils.isEmpty(entityMap)) {
                if(!StringUtils.isEmpty(entityMap.get("bookId"))) {
                    entity.setBookId(Integer.valueOf(String.valueOf(entityMap.get("bookId"))));
                }
                if(!StringUtils.isEmpty(entityMap.get("bookDate"))) {
                    entity.setBookDate(String.valueOf(entityMap.get("bookDate")));
                }
                if (!StringUtils.isEmpty(entityMap.get("bookName"))){
                    entity.setBookName(String.valueOf(entityMap.get("bookName")));
                }
                if (!StringUtils.isEmpty(entityMap.get("author"))){
                    entity.setAuthor(String.valueOf(entityMap.get("author")));
                }
                if (!StringUtils.isEmpty(entityMap.get("press"))){
                    entity.setPress(String.valueOf(entityMap.get("press")));
                }
                if(!StringUtils.isEmpty(entityMap.get("bookDesc"))) {
                    entity.setBookDesc(String.valueOf(entityMap.get("bookDesc")));
                }
                if(!StringUtils.isEmpty(entityMap.get("bookType"))) {
                    entity.setBookType(String.valueOf(entityMap.get("bookType")));
                }
                if(!StringUtils.isEmpty(entityMap.get("photo"))) {
                    entity.setPhoto(String.valueOf(entityMap.get("photo")));
                }
                if(!StringUtils.isEmpty(entityMap.get("cnt"))) {
                    entity.setCnt(Integer.valueOf(String.valueOf(entityMap.get("cnt"))));
                }
            }
            list.add(entity);
        }
        PageInfo result = new PageInfo(list);
        return result;
    }
}
