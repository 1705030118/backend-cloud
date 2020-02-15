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
//    public SearchDomain(SearchActivityDomain searchActivityDomain){
//        setActivityId(searchActivityDomain.getActivityId());
//        setUserId(searchActivityDomain.getUserId());
//        setPublishTime(searchActivityDomain.getPublishTime());
//        setActivityType(searchActivityDomain.getActivityType());
//        setActivityName(searchActivityDomain.getActivityName());
//        setLocationName(searchActivityDomain.getLocationName());
//    }
}
