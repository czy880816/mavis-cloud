package com.mavis.apollo.demo.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    @Value("${jasypt.encryptor.password}")
    private volatile String pwd;
    @Autowired
    private TestConfig config;

    //@GetMapping("/getOther")
    public Map<String, Object> getConfig() {
        Map<String, Object> res = new HashMap<>();
        res.put("input", config.getInput());
        res.put("input1", config.getInput1());
        res.put("pwd", pwd);
        System.out.println(System.getProperty("apollo.autoUpdateInjectedSpringProperties"));
        return res;
    }


}
