package com.example.demozookeeperkafka;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@SpringBootApplication
public class DemoZookeeperKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoZookeeperKafkaApplication.class, args);
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @SneakyThrows
    @KafkaListener(topics = "Topic1")
    public void recordLoginTime(ConsumerRecord<byte[], byte[]> cr) {

        log.info("cr: {}",cr);
    }

    @GetMapping("/")
    public String home() {
        return this.serviceUrl();
    }

    public String serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances("STORES");
        if (list != null && !list.isEmpty()) {
            return list.get(0).getUri().toString();
        }
        return null;
    }
}
