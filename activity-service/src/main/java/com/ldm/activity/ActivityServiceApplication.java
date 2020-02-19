package com.ldm.activity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.ldm.activity")
public class ActivityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityServiceApplication.class, args);
    }

}
