package com.mavis.apollo.demo.app;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.mavis.apollo.demo.app.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableApolloConfig
@RestController
//@EnableConfigurationProperties(TestConfig.class)
public class ApolloTestApp {
    public static void main(String[] args) {
        SpringApplication.run(ApolloTestApp.class, args);
        //new SpringApplicationBuilder().environment(new StandardEncryptableEnvironment()).sources(ApolloTestApp.class).run(args);
    }

    @Autowired
    private TestConfig config;

    @ApolloConfig
    private Config cg;

    @Autowired
    private ApplicationContext context;

    @Value("${jasypt.encryptor.password}")
    private volatile String pwd;


    @GetMapping("/getConfig")
    public Map<String, Object> getConfig() {
        Map<String, Object> res = new HashMap<>();
        ApolloTestApp apolloTestApp = context.getBean(ApolloTestApp.class);
        res.put("input", config.getInput());
        res.put("input1", config.getInput1());
        res.put("pwd", cg.getProperty("jasypt.encryptor.password", "default"));
        res.put("pwd1", pwd);
        res.put("pwd2", apolloTestApp.getPwd());
        return res;
    }

    /*@Bean
    public TestConfig testConfig() {
        return new TestConfig();
    }*/

    @ApolloConfigChangeListener("application")
    private void anotherOnChange(ConfigChangeEvent changeEvent) {
        System.out.println(changeEvent.changedKeys());
        boolean redisCacheKeysChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("test")) {
                redisCacheKeysChanged = true;
                break;
            }
        }
        if (!redisCacheKeysChanged) {
            return;
        }
        //scope.refresh("testConfig");
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
