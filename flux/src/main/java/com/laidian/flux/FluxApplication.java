package com.laidian.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.*;

@SpringBootApplication
@RestController
public class FluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class, args);
    }

    //使用自定义伸缩线程池
    private static ExecutorService fix= Executors.newFixedThreadPool(150);

    @GetMapping("/hello/{lantency}")
    public Mono<String> hello(@PathVariable Long lantency) throws InterruptedException {
        return Mono.fromFuture(CompletableFuture.supplyAsync(()->{
            try {
               return getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        },fix));
    }

    private String getResult() throws InterruptedException {
        Thread.sleep(100);
        return "success";
    }
}

