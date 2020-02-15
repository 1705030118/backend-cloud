package com.ldm.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "app",type = "log")
public class SearchLogDomain{
    @Id
    private int logId;
    private int userId;
    private String type;
    private String title;
    private String url;
    private String method;
    private Object params;
    private String exception;
    private String beginTime;
    private String endTime;
}