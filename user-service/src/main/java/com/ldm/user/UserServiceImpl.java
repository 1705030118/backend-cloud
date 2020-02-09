package com.ldm.user;

import com.ldm.api.CacheService;
import com.ldm.api.UserService;
import com.ldm.util.UUIDUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CacheService cacheService;
    @Override
    public String login(String phone, String password) {
        if(!cacheService.exists("user:phone:"+phone)){
            User user=userDao.checkLogin(phone);
            if(user==null) return null;// 用户不存在
            if(password.equals(MD5Util.formPassToDbPass(password,user.getSalt()))){
                String token= UUIDUtil.uuid();
                cacheService.delete("token:"+user.getUserId());
                cacheService.set("token:"+user.getUserId(),token,"NX","EX",30*60);
                return token;
            }
        }else{

        }
        return null;
    }

    @Override
    public Object register(String phone) {
        return null;
    }

    @Override
    public Object logout(int userId, String token) {
        return null;
    }

    public Object sendSms(String phone){
        String host = "http://smsmsgs.market.alicloudapi.com";
        String path = "/smsmsgs";
        String method = "GET";
        System.out.println("请先替换成自己的AppCode");
        String appcode = "80fb8766399643338e876d9be04c294c";  // !!! 替换这里填写你自己的AppCode 请在买家中心查看
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode); //格式为:Authorization:APPCODE 83359fd73fe11248385f570e3c139xxx
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("param", "郭靖|12|5000");// !!! 请求参数
        querys.put("phone", "17774621486");// !!! 请求参数
        querys.put("sign", "1");
        querys.put("skin", "1002");// !!! 请求参数
        try {
            HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString()); //输出头部
            System.out.println(response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode()==200)
                System.out.println(EntityUtils.toString(response.getEntity())); //输出json
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
