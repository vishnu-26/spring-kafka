package com.example.kafkastarter.web;

import com.example.kafkastarter.event.NotificationEvent;
import com.example.kafkastarter.service.NotificationProducer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationProducer producer;

    public NotificationController(NotificationProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationEvent publish(@Valid @RequestBody NotificationRequest request) {
        NotificationEvent event = new NotificationEvent(
                UUID.randomUUID().toString(), request.recipient(), request.message(), Instant.now());
        producer.send(event);
        return event;
    }
}
