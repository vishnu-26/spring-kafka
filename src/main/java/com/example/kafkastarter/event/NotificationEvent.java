package com.example.kafkastarter.event;

import java.time.Instant;

public record NotificationEvent(String id, String recipient, String message, Instant createdAt) { }
