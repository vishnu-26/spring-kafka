package com.example.kafkastarter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
class KafkaStarterApplicationTests {
    @Test
    void contextLoads() { }
}
