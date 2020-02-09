package com.ldm.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String mobile;
    private String password;
    private String token;
}
