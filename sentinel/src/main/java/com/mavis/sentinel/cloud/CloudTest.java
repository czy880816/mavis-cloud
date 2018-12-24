package com.mavis.sentinel.cloud;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

public class CloudTest {

    @SentinelResource("resource")
    public String hello() {
        return "Hello";
    }
}
