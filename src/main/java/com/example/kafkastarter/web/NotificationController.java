package com.example.kafkastarter.web;

import com.example.kafkastarter.event.NotificationEvent;
import com.example.kafkastarter.service.BlockingNotificationProducer;
import com.example.kafkastarter.service.CallbackNotificationProducer;
import com.example.kafkastarter.service.SimpleNotificationProducer;
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
    private final SimpleNotificationProducer simpleProducer;
    private final BlockingNotificationProducer blockingProducer;
    private final CallbackNotificationProducer callbackProducer;

    public NotificationController(SimpleNotificationProducer simpleProducer,
                                  BlockingNotificationProducer blockingProducer,
                                  CallbackNotificationProducer callbackProducer) {
        this.simpleProducer = simpleProducer;
        this.blockingProducer = blockingProducer;
        this.callbackProducer = callbackProducer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationEvent publishSimple(@Valid @RequestBody NotificationRequest request) {
        NotificationEvent event = notificationEvent(request);
        simpleProducer.send(event);
        return event;
    }

    @PostMapping("/blocking")
    public NotificationEvent publishBlocking(@Valid @RequestBody NotificationRequest request) {
        NotificationEvent event = notificationEvent(request);
        blockingProducer.send(event);
        return event;
    }

    @PostMapping("/callback")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationEvent publishWithCallback(@Valid @RequestBody NotificationRequest request) {
        NotificationEvent event = notificationEvent(request);
        callbackProducer.send(event);
        return event;
    }

    private NotificationEvent notificationEvent(NotificationRequest request) {
        return new NotificationEvent(
                UUID.randomUUID().toString(), request.recipient(), request.message(), Instant.now());
    }
}
