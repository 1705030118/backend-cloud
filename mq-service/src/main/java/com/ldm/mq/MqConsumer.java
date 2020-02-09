package com.ldm.mq;

import com.ldm.api.CacheService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class MqConsumer {
    @Autowired
    private CacheService cacheService;
    @RabbitListener(queues = MQConfig.LIKE_DYNAMIC_QUEUE)
    public void likeDynamic(){
    }
}
