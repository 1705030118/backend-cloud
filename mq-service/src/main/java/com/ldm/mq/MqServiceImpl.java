package com.ldm.mq;

import com.ldm.api.MqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class MqServiceImpl implements MqService, RabbitTemplate.ConfirmCallback{
    private static Logger logger = LoggerFactory.getLogger(MqServiceImpl.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void likeDynamic(int activity, int userId) {
        logger.info("MQ send message: ");
        amqpTemplate.convertAndSend(MQConfig.LIKE_DYNAMIC_QUEUE,new Object());
    }

    @Override
    public void sendCode() {
        CorrelationData skCorrData = new CorrelationData(UUID.randomUUID().toString());
    }
    /**
     * MQ ack 机制
     * TODO 完善验证机制，确保消息能够被消费，且不影响消息吞吐量
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("SkMessage UUID: " + correlationData.getId());
        if (ack) {
            logger.info("SkMessage 消息消费成功！");
        } else {
            System.out.println("SkMessage 消息消费失败！");
        }

        if (cause != null) {
            logger.info("CallBackConfirm Cause: " + cause);
        }
    }
}
