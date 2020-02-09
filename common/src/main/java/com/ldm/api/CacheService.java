package com.ldm.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "cache-service")
public interface CacheService {
    /**
     * redis 的get操作，通过key获取存储在redis中的对象
     *
     * @param key    业务层传入的key
     * @param clazz  存储在redis中的对象类型（redis中是以字符串存储的）
     * @param <T>    指定对象对应的类型
     * @return 存储于redis中的对象
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * redis的set操作
     *
     * @param key    键
     * @param value  值
     * @return 操作成功为true，否则为false
     */
    <T> boolean set(String key, T value);

    /**
     * redis的set操作
     *
     * @param key    键
     * @param value  值
     * @return 操作成功为true，否则为false
     */
    <T> boolean set(String key, T value,String nxxx,String expx,int expireSeconds);

    /**
     * 判断key是否存在于redis中
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    long incr(String key);

    /**
     * 自减
     *
     * @param key
     * @return
     */
    long decr(String key);


    /**
     * 删除缓存中的用户数据
     *
     * @param key
     * @return
     */
    boolean delete(String key);
}
