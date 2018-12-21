package com.mavis.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.mavis.apollo.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableApolloConfig
@RestController
public class ApolloTestApp {
    public static void main(String[] args) {
        SpringApplication.run(ApolloTestApp.class, args);
    }

    @Autowired
    private TestConfig config;

    @ApolloConfig
    private Config cg;

    @GetMapping("/getConfig")
    public Map<String, Object> getConfig() {
        Map<String, Object> res = new HashMap<>();
        res.put("input", config.getInput());
        res.put("input1", config.getInput1());
        return res;
    }

    @Bean
    public TestConfig testConfig() {
        return new TestConfig();
    }

    @ApolloConfigChangeListener("application")
    private void anotherOnChange(ConfigChangeEvent changeEvent) {
        //do something
        System.out.println(changeEvent.changedKeys());
        System.out.println(cg.getPropertyNames());
    }
}
