package com.ldm.dynamic;

import com.ldm.request.DynamicRequest;
import org.apache.ibatis.annotations.Insert;

public interface DynamicDao {
    @Insert("insert into t_dynamic values(null,#{content},#{userId},#{publishTime})")
    int publish(DynamicRequest dynamicRequest);
}
