package com.ldm.service;

import com.ldm.api.TestService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestServiceImpl implements TestService {
    @Override
    public String print() {
        return "hello world!!!";
    }
}
