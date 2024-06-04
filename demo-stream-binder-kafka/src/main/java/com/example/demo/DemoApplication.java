package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping
@SpringBootApplication
public class DemoApplication {

    @Autowired
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @SneakyThrows
    @GetMapping("hello")
    public String hello() {
        Person person = new Person();
        person.setName(UUID.randomUUID().toString());
        MessageBuilder<Person> data = MessageBuilder.withPayload(person);
        streamBridge.send("audit-out", MessageBuilder.withPayload("audit event start: " + person).build());

        //complex logic.
        streamBridge.send("consumer-out", data.build());

        streamBridge.send("audit-out", MessageBuilder.withPayload("audit event end: " + person).build());

        return "Hello ";
    }

    @Bean
    public Consumer<Message<String>> auditLog() {
        return message ->{
            log.info("auditLog Headers : {}", message.getHeaders());
            log.info("auditLog Payload : {}", message.getPayload());
        };
    }
    @Bean
    public Consumer<Message<Person>> personConsumer() {
        return message ->{
            log.info("person Headers : {}", message.getHeaders());
            Person payload = message.getPayload();
            log.info("person Payload : {}", payload);
        };
    }

    public static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
