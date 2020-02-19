package com.ldm.domain;
import lombok.Data;
import java.util.List;
@Data
public class ActivityDomain {
    private int activityId;
    private String activityName;
    private int userId;
    private String activityType;
    private String locationName;
    private String publishTime;
    private String userNickname;
    public SearchActivityDomain transform(){
        SearchActivityDomain searchActivityDomain=new SearchActivityDomain();
        searchActivityDomain.setActivityId(activityId);
        searchActivityDomain.setActivityName(activityName);
        searchActivityDomain.setActivityType(activityType);
        searchActivityDomain.setActivityType(activityType);
        searchActivityDomain.setPublishTime(publishTime);
        searchActivityDomain.setUserId(userId);
        return searchActivityDomain;
    }
}