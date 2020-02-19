package com.ldm.activity;

import com.ldm.domain.ActivityDomain;
import com.ldm.request.ActivityRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import java.util.List;
public interface ActivityDao {
    // 发布活动
    @Insert("insert into t_activity values(null,#{userId},#{activityName},#{activityType},#{locationName},#{publishTime})")
    int publish(ActivityRequest activityRequest);
    @Select("SELECT * FROM t_activity LIMIT 0,10")
    List<ActivityDomain> selectAll();
    List<Activity> getAll();
}
