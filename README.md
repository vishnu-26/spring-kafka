# Spring Boot Kafka Starter

A minimal Java 21 / Spring Boot application that creates a Kafka topic, publishes JSON messages through a REST endpoint, and consumes those messages in the same application.

## Start it

Prerequisites: Java 21, Maven 3.9+, and Docker Desktop.

```powershell
docker compose up -d
mvn spring-boot:run
```

The service runs at `http://localhost:8080`, and Kafka is exposed at `localhost:29092`. Spring creates the `notifications.v1` topic automatically at startup.

## Publish a message

```powershell
$body = @{ recipient = 'vishnu@example.com'; message = 'Kafka is working!' } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/notifications -ContentType application/json -Body $body
```

The API returns `202 Accepted`; the application console prints the consumed event. Kafka partitions records by recipient, preserving order for each recipient.

## Project layout

- `web/NotificationController` exposes the HTTP publishing endpoint.
- `service/NotificationProducer` sends JSON events to Kafka.
- `service/NotificationConsumer` handles topic events using `@KafkaListener`.
- `config/KafkaTopicConfiguration` declares the Kafka topic.
- `application.yml` holds broker, serializer, and consumer-group configuration.

To point to a different broker, set `KAFKA_BOOTSTRAP_SERVERS`. To change the topic, update `app.kafka.topic`.
