package com.example.demologsconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;

@Slf4j
@EnableAsync
@Configuration
@SpringBootApplication
public class DemoLogsConfigApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoLogsConfigApplication.class, args);
    }

    @Override
    @Async
    public void run(String... args) throws InterruptedException {
        Runnable runnable = () -> {
            for (;;) {
                log.info("public class config implements CommandLineRunner " + LocalDateTime.now());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {
                }
            }
        };
        runnable.run();

    }
}
