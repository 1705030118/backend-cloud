package com.ldm.user;

import lombok.Data;

@Data
public class User {
    private String userId;
    private String phone;
    private String password;
    private String userNickname;
    private String userName;
    private String salt;
}
