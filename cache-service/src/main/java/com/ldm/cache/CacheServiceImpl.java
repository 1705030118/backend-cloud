package com.ldm.cache;

import com.alibaba.fastjson.JSON;
import com.ldm.api.CacheService;
import com.ldm.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
@RefreshScope
@RestController
public class CacheServiceImpl implements CacheService {

    /**
     * 通过连接池对象可以获得对redis的连接
     */
    @Autowired
    JedisPool jedisPool;

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;// redis连接

        try {
            jedis = jedisPool.getResource();
            // 通过key获取存储于redis中的对象（这个对象是以json格式存储的，所以是字符串）
            String strValue = jedis.get(key);
            // 将json字符串转换为对应的对象
            T objValue = JsonUtil.stringToBean(strValue, clazz);
            return objValue;
        } finally {
            // 归还redis连接到连接池
            returnToPool(jedis);
        }
    }

    @Override
    public <T> boolean set(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 将对象转换为json字符串
            String strValue = JsonUtil.beanToString(value);
            if (strValue == null || strValue.length() <= 0)
                return false;
            jedis.set(key, strValue);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public <T> boolean set(String key, T value, String nxxx, String expx, int expireSeconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 将对象转换为json字符串
            String strValue = JsonUtil.beanToString(value);
            if (strValue == null || strValue.length() <= 0)
                return false;
            jedis.set(key,strValue,nxxx,expx,expireSeconds);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(key);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long del = jedis.del(key);
            return del > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 点赞帖子
     * @param dynamicId
     * @param userId
     * @return
     */
    public void likeDynamic(int dynamicId,int userId){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key="like:dynamic:"+dynamicId;
            if(jedis.sismember(key,""+userId)) jedis.srem(key,""+userId);
            else jedis.sadd(key,""+userId);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 用户操作频率限制,如点赞、发布活动、评论
     * @param userId
     * @return
     */
    public boolean limitFrequency(int userId){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long nowTs=System.currentTimeMillis();
            int period=60,maxCount=5;
            String key="frequency:limit:"+userId;
            jedis.zadd(key,nowTs,""+nowTs);
            jedis.zremrangeByScore(key,0,nowTs-period*1000);
            return jedis.zcard(key)>maxCount;
        } finally {
            returnToPool(jedis);
        }
    }
    /**
     * 将redis连接对象归还到redis连接池
     *
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }

}