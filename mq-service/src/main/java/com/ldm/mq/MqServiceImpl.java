package com.ldm.mq;

import com.ldm.api.MqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MqServiceImpl implements MqService {
    private static Logger logger = LoggerFactory.getLogger(MqServiceImpl.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void likeDynamic(int activity, int userId) {
        logger.info("MQ send message: ");
        amqpTemplate.convertAndSend(MQConfig.LIKE_DYNAMIC_QUEUE,new Object());
    }
}
