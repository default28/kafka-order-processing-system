# Kafka Order Processing System

A distributed event-driven microservices project built using Spring Boot, Apache Kafka, PostgreSQL, Docker, and Kubernetes concepts.

## Overview

This project demonstrates how orders are asynchronously processed using Apache Kafka. The producer service publishes order events to Kafka, while the consumer service consumes those events and persists them into PostgreSQL.

The project showcases key distributed systems concepts such as:

* Event-driven architecture
* Apache Kafka Producer and Consumer
* Kafka Consumer Groups
* Topic Partitions
* Offset Management
* Idempotent Consumer Pattern
* PostgreSQL Persistence
* Spring Data JPA
* Dockerized Infrastructure

---

## Architecture

```text
                POST /orders
                       │
                       ▼
              Producer Service
                       │
                       ▼
                Kafka Topic
                 orders-topic
                       │
                       ▼
              Consumer Service
                       │
                       ▼
                  PostgreSQL
```

---

## Tech Stack

### Backend

* Java 21
* Spring Boot 3
* Spring Data JPA
* Spring Kafka

### Messaging

* Apache Kafka

### Database

* PostgreSQL

### Infrastructure

* Docker
* Kubernetes (concepts and deployment experience)

---

## Project Structure

```text
kafka-order-processing-system
│
├── producer-service
│   ├── controller
│   ├── service
│   └── model
│
├── consumer-service
│   ├── consumer
│   ├── entity
│   ├── repository
│   └── model
│
└── docker-compose.yml
```

---

## Features

### Producer Service

* REST API for order creation
* Publishes events to Kafka
* Uses KafkaTemplate
* JSON serialization

### Consumer Service

* Consumes order events from Kafka
* Processes events using @KafkaListener
* Stores data in PostgreSQL
* Demonstrates idempotent processing

### Kafka

* Topic based communication
* Consumer Groups
* Offset tracking
* Partition based scaling

---

## Kafka Concepts Demonstrated

### Producer

Publishes order events to Kafka.

```java
kafkaTemplate.send(
    "orders-topic",
    order.getOrderId().toString(),
    order
);
```

### Consumer Group

```properties
spring.kafka.consumer.group-id=order-group
```

Kafka distributes partitions among consumers in the same group.

### Partitions

Messages are distributed across partitions using the order id as the message key.

### Offsets

Kafka tracks the last processed message offset for each consumer group.

### Idempotency

Duplicate messages are safely ignored using:

```java
repository.existsById(orderId)
```

---

## API

### Create Order

```http
POST /orders
```

Request:

```json
{
  "orderId": 1,
  "customer": "Mini",
  "amount": 500
}
```

Response:

```text
Order Published Successfully
```

---

## Running Locally

### Start Kafka

```bash
docker compose up -d
```

### Start Producer

```bash
cd producer-service

mvn spring-boot:run
```

### Start Consumer

```bash
cd consumer-service

mvn spring-boot:run
```

### Publish Event

```bash
curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d '{
  "orderId":1,
  "customer":"Mini",
  "amount":500
}'
```

---

## Sample Flow

```text
POST /orders
      ↓
Producer Service
      ↓
Kafka Topic
      ↓
Consumer Service
      ↓
PostgreSQL
```

---

## Future Enhancements

* Dead Letter Topic (DLT)
* Retry Mechanism
* Kafka on Kubernetes
* Monitoring using Prometheus and Grafana
* Schema Registry
* Avro Serialization
* CI/CD Pipeline

---

## Learning Outcomes

Through this project, I gained hands-on experience with:

* Event-driven microservices
* Kafka Producer and Consumer development
* Consumer Group scaling
* Partition assignment and rebalancing
* Offset management
* Spring Boot and PostgreSQL integration
* Distributed systems fundamentals

## build commands
```bash
mvn spring-boot:run

start kafka docker container
docker compose up -d
docker ps

create kafka topic
docker exec -it kafka bash

/opt/kafka/bin/kafka-topics.sh \
--create \
--topic orders-topic \
--bootstrap-server localhost:9092 \
--partitions 3 \
--replication-factor 1

verify if topic exists
/opt/kafka/bin/kafka-topics.sh \
--list \
--bootstrap-server localhost:9092

verify parititons
docker exec -it kafka \
/opt/kafka/bin/kafka-topics.sh \
--describe \
--topic orders-topic \
--bootstrap-server localhost:9092

check if message is published on kafka
curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d '{
"orderId":1,
"customer":"Mini",
"amount":500
}'

verify message is actually reached kafka
docker exec -it kafka bash

start a console consumer
/opt/kafka/bin/kafka-console-consumer.sh \
--topic orders-topic \
--bootstrap-server localhost:9092 \
--from-beginning

after adding postgres
docker compose up -d

verify postgresql
docker exec -it postgres psql -U admin -d postgres

if database doesnt exists
CREATE DATABASE ordersdb;

connect to db
docker exec -it postgres psql -U admin -d ordersdb

ordersdb=# select * from orders;

for i in {21..40}
do
curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d "{\"orderId\":$i,\"customer\":\"Mini\",\"amount\":500}"
done

create dead letter topic
docker exec -it kafka bash

/opt/kafka/bin/kafka-topics.sh \
--create \
--topic orders-dlt \
--bootstrap-server localhost:9092
```
