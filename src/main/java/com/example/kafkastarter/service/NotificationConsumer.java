package com.example.kafkastarter.service;

import com.example.kafkastarter.event.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    @KafkaListener(topics = "${app.kafka.topic}")
    public void consume(NotificationEvent event) {
        System.out.printf("Received notification %s for %s: %s%n",
                event.id(), event.recipient(), event.message());
    }
}
