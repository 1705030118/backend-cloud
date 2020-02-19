package com.ldm.dynamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DynamicServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicServiceApplication.class, args);
    }

}
