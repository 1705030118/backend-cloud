package com.ldm.user;

public interface UserDao {
    int login(String phone,String password);
    User checkLogin(String phone);
}
