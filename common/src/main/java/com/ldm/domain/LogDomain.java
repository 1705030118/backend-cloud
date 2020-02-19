package com.ldm.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志类-记录用户操作行为
 */
@Data
public class LogDomain implements Serializable {
    private static final long serialVersionUID = 1L;
    private int logId;
    private int userId;
    private String logType;
    private String title;
    private String url;
    private String method;
    private Object params;
    private String exception;
    private String beginTime;
    private String endTime;
}