package com.laidian.web;

import com.laidian.common.ApplicationEnvironmentPreparedEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(WebApplication.class);
        //必须在启动前插入监听器,不能以bean的形式注入,否则无法处理自定义表达式
        application.addListeners(new ApplicationEnvironmentPreparedEventListener());
        application.run(args)
        //此处的监听器只能监听ContextRefreshedEvent,ContextClosedEvent两种事件
        //.addApplicationListener()
        ;
    }

    @GetMapping("/hello/{lantency}")
    public String hello(@PathVariable Long lantency) {
        try {
            TimeUnit.MILLISECONDS.sleep(lantency);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @Value("${server.port}")
    private Integer port1;
    @Value("${server1.port}")
    private Integer port2;

    @GetMapping("/getPort")
    public String getPort() {
        return port1 + ";" + port2;
    }

}

