package com.ldm.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "backend",type = "activity")
public class SearchDomain {
    @Id
    private Integer activityId;
    private int userId;
    @Field(analyzer = "ik_max_word")
    private String activityName;
    @Field(analyzer = "ik_max_word")
    private String activityType;
    @Field(analyzer = "ik_max_word")
    private String locationName;
    private String publishTime;
    private String userNickname;
}
