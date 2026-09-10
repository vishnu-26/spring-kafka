package com.example.kafkastarter.service;

import com.example.kafkastarter.event.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/** Sends a record asynchronously and reacts to the broker acknowledgement in a callback. */
@Service
public class CallbackNotificationProducer {
    private static final Logger log = LoggerFactory.getLogger(CallbackNotificationProducer.class);

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final String topic;

    public CallbackNotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate,
                                        @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(NotificationEvent event) {
        kafkaTemplate.send(topic, event.recipient(), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Failed to send notification {}", event.id(), exception);
                        return;
                    }

                    var metadata = result.getRecordMetadata();
                    log.info("Sent notification {} to {}-{} at offset {}",
                            event.id(), metadata.topic(), metadata.partition(), metadata.offset());
                });
    }
}
