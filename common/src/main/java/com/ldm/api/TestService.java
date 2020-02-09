package com.ldm.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "test-service")
public interface TestService {
    @GetMapping
    String print();
}
