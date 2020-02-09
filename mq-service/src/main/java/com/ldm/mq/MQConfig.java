package com.ldm.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 通过配置文件获取消息队列
 *
 * @author noodle
 */
@Configuration
public class MQConfig {

    /**
     * 消息队列名
     */
    public static final String LIKE_DYNAMIC_QUEUE = "like.dynamic.queue";

    /**
     * Direct模式 交换机exchange
     * 生成用于秒杀的queue
     *
     * @return
     */
    @Bean
    public Queue likeDynamicQueue() {
        return new Queue(LIKE_DYNAMIC_QUEUE, true);
    }

    /**
     * 实例化 RabbitTemplate
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }
}
