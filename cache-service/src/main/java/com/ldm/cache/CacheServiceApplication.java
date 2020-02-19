package com.ldm.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class CacheServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheServiceApplication.class, args);
    }

}
