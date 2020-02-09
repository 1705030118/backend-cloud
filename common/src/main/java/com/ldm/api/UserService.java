package com.ldm.api;

public interface UserService {
    /**
     * 登录返回token，token缓存在redis中
     * @param phone 用户名
     * @param password MD5加密过的phone
     * @return token
     */
    String login(String phone,String password);
    Object register(String phone);
    Object logout(int userId,String token);
}
