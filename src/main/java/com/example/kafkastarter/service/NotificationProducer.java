package com.example.kafkastarter.service;

import com.example.kafkastarter.event.NotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final String topic;

    public NotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate,
                                @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(NotificationEvent event) {
        kafkaTemplate.send(topic, event.recipient(), event);
    }
}
