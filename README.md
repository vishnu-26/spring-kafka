# Spring Boot Kafka Starter

A minimal Java 21 / Spring Boot application that creates a Kafka topic, publishes JSON messages through a REST endpoint, and consumes those messages in the same application.

## Start it with Docker

Prerequisite: Docker Desktop. The application is built and run using Java 21
inside its container, so no local Java or Maven installation is required.

```powershell
docker compose up --build
```

The service runs at `http://localhost:8080`, and Kafka is exposed at `localhost:29092`. Spring creates the `notifications.v1` topic automatically at startup.

To run in the background, use `docker compose up --build -d`. View application
logs with `docker compose logs -f app`, and stop the stack with `docker compose down`.

## Run locally (optional)

If you prefer running the application outside Docker, install Java 21 and Maven
3.9+, start Kafka with `docker compose up -d kafka`, then run:

```powershell
mvn spring-boot:run
```

## Publish a message

All endpoints accept the same JSON body. The default endpoint is the simple
asynchronous producer: it starts a Kafka send and immediately returns `202 Accepted`.

```powershell
$body = @{ recipient = 'vishnu@example.com'; message = 'Kafka is working!' } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/notifications -ContentType application/json -Body $body
```

The API returns `202 Accepted`; the application console prints the consumed event. Kafka partitions records by recipient, preserving order for each recipient.

### Blocking producer

This endpoint waits for Kafka to acknowledge the record before returning.

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/notifications/blocking -ContentType application/json -Body $body
```

### Callback producer

This endpoint returns `202 Accepted` immediately and logs either the Kafka record
metadata after acknowledgement or the send failure from its completion callback.

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/notifications/callback -ContentType application/json -Body $body
```

## Project layout

- `web/NotificationController` exposes the HTTP publishing endpoint.
- `service/SimpleNotificationProducer` sends JSON events without waiting.
- `service/BlockingNotificationProducer` waits for Kafka's acknowledgement.
- `service/CallbackNotificationProducer` handles acknowledgement or failure asynchronously.
- `service/NotificationConsumer` handles topic events using `@KafkaListener`.
- `config/KafkaTopicConfiguration` declares the Kafka topic.
- `application.yml` holds broker, serializer, and consumer-group configuration.

To point to a different broker, set `KAFKA_BOOTSTRAP_SERVERS`. To change the topic, update `app.kafka.topic`.
