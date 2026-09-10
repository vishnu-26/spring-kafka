package com.example.kafkastarter.service;

import com.example.kafkastarter.event.NotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/** Sends a record asynchronously without waiting for Kafka to acknowledge it. */
@Service
public class SimpleNotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final String topic;

    public SimpleNotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate,
                                      @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(NotificationEvent event) {
        kafkaTemplate.send(topic, event.recipient(), event);
    }
}
