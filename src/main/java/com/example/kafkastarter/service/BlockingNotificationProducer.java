package com.example.kafkastarter.service;

import com.example.kafkastarter.event.NotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

/** Sends a record and waits until Kafka acknowledges it or the send fails. */
@Service
public class BlockingNotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final String topic;

    public BlockingNotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate,
                                        @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public SendResult<String, NotificationEvent> send(NotificationEvent event) {
        try {
            return kafkaTemplate.send(topic, event.recipient(), event).get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while sending notification", exception);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to send notification", exception);
        }
    }
}
