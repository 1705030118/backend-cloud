package com.ldm.domain;

import lombok.Data;

import java.util.List;

@Data
public class DynamicDomain {
    private int dynamicId;
    private String userId;
    private String userNickname;
    private String publishTime;
    private String content;
}