package com.ldm.search;

import com.ldm.domain.LogDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "sys-log",type = "log")
public class SearchLogDomain{
    @Id
    private int logId;
    private int userId;
    private String logType;
    @Field(analyzer = "ik_max_word")
    private String title;
    @Field(analyzer = "ik_max_word")
    private String url;
    private String method;
    private Object params;
    @Field(analyzer = "ik_max_word")
    private String exception;
    private String beginTime;
    private String endTime;
    public static SearchLogDomain transform(LogDomain logDomain){
        SearchLogDomain searchLogDomain=new SearchLogDomain();
        searchLogDomain.setBeginTime(logDomain.getBeginTime());
        searchLogDomain.setEndTime(logDomain.getEndTime());
        searchLogDomain.setUrl(logDomain.getUrl());
        searchLogDomain.setMethod(logDomain.getMethod());
        searchLogDomain.setParams(logDomain.getParams());
        searchLogDomain.setTitle(logDomain.getTitle());
        searchLogDomain.setLogType(logDomain.getLogType());
        searchLogDomain.setUserId(logDomain.getUserId());
        searchLogDomain.setLogId(logDomain.getLogId());
        return searchLogDomain;
    }
}