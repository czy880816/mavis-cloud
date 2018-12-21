package com.laidian.spock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {
    @Value("${name:World}")
    private String name;

    public String getHelloMessage() {
        return "Hello " + this.name;
    }
}
