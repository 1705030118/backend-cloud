package com.ldm.gateway.async;

import lombok.Data;

@Data
public class Log {
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
