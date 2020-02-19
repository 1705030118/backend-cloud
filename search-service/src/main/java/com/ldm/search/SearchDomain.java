package com.ldm.search;

import com.ldm.domain.SearchActivityDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "app",type = "activity")
public class SearchDomain {
    @Id
    private Integer activityId;
    private Integer userId;
    @Field(analyzer = "ik_max_word")
    private String activityName;
    private String activityType;
    @Field(analyzer = "ik_max_word")
    private String locationName;
    private String publishTime;
    public static SearchDomain transform(SearchActivityDomain searchActivityDomain){
        SearchDomain searchDomain=new SearchDomain();
        searchDomain.setActivityId(searchActivityDomain.getActivityId());
        searchDomain.setUserId(searchActivityDomain.getUserId());
        searchDomain.setPublishTime(searchActivityDomain.getPublishTime());
        searchDomain.setActivityType(searchActivityDomain.getActivityType());
        searchDomain.setActivityName(searchActivityDomain.getActivityName());
        searchDomain.setLocationName(searchActivityDomain.getLocationName());
        return searchDomain;
    }
    public static SearchActivityDomain transform2(SearchDomain searchDomain){
        SearchActivityDomain searchActivityDomain=new SearchActivityDomain();
        searchActivityDomain.setActivityId(searchDomain.getActivityId());
        searchActivityDomain.setUserId(searchDomain.getUserId());
        searchActivityDomain.setPublishTime(searchDomain.getPublishTime());
        searchActivityDomain.setActivityType(searchDomain.getActivityType());
        searchActivityDomain.setActivityName(searchDomain.getActivityName());
        searchActivityDomain.setLocationName(searchDomain.getLocationName());
        return searchActivityDomain;
    }
}
