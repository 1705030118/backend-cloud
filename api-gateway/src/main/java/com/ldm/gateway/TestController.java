package com.ldm.gateway;

import com.ldm.api.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/info")
    public String test(){
        System.out.println("hit hit");
        return testService.print();
    }
}
