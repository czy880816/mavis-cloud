package com.mavis.apollo.demo.app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@RefreshScope
//@ConfigurationProperties(prefix = "test")
@Component("testConfig")
@Data
public class TestConfig {

    @Value("${test.input}")
    private String input;
    @Value("${test.input1}")
    private String input1;
}
