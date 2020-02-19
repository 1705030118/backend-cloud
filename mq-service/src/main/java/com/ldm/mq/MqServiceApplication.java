package com.ldm.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MqServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqServiceApplication.class, args);
    }

}
