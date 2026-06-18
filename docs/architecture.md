# Architecture

## High Level Architecture

```text
                ┌─────────────────────┐
                │   Producer Service  │
                │ Spring Boot REST API│
                └──────────┬──────────┘
                           │
                           │ Publish Order Event
                           ▼
                ┌─────────────────────┐
                │     Apache Kafka    │
                │    orders-topic     │
                └──────────┬──────────┘
                           │
                           │ Consume Event
                           ▼
                ┌─────────────────────┐
                │   Consumer Service  │
                │  Spring Kafka       │
                └──────────┬──────────┘
                           │
                           │ Persist Order
                           ▼
                ┌─────────────────────┐
                │     PostgreSQL      │
                └─────────────────────┘
```

---

## Event Flow

### Step 1

Client sends:

```http
POST /orders
```

### Step 2

Producer Service receives request and creates an OrderEvent.

```json
{
  "orderId": 1,
  "customer": "Mini",
  "amount": 500
}
```

### Step 3

Producer publishes event to Kafka.

```java
kafkaTemplate.send(
    "orders-topic",
    order.getOrderId().toString(),
    order
);
```

### Step 4

Kafka stores the event in a partition.

```text
orders-topic

Partition 0
Partition 1
Partition 2
```

### Step 5

Consumer Service receives the event through KafkaListener.

### Step 6

Consumer converts the event into a database entity.

### Step 7

Order is persisted into PostgreSQL.

---

## Consumer Group Architecture

```text
                 orders-topic

      Partition 0
      Partition 1
      Partition 2

             │
             ▼

      Consumer Group

      Consumer 1
      Consumer 2
      Consumer 3
```

Kafka distributes partitions among consumers to achieve parallel processing.

---

## Offset Management

```text
Partition 1

Offset 0
Offset 1
Offset 2
Offset 3
```

Kafka tracks processed offsets for each consumer group.

This enables:

* Fault tolerance
* Consumer recovery
* Message replay

---

## Idempotent Processing

To avoid duplicate database records:

```java
if(repository.existsById(
        event.getOrderId())) {
    return;
}
```

This ensures safe processing under at-least-once delivery semantics.

---

## Design Decisions

### Why Kafka?

* Decouples producer and consumer
* Improves scalability
* Enables asynchronous processing
* Supports replayability

### Why PostgreSQL?

* Relational data model
* ACID compliance
* Strong consistency

### Why Consumer Groups?

* Horizontal scalability
* Fault tolerance
* Parallel message processing

---

## Scalability Considerations

### Scale Producers

Multiple producer instances can publish events concurrently.

### Scale Consumers

Increase consumer instances to match topic partitions.

Example:

```text
3 Partitions
3 Consumers
```

All consumers process messages in parallel.

---

## Failure Handling

Current implementation:

* At-Least-Once Delivery
* Idempotent Consumer

Future enhancements:

* Retry Topics
* Dead Letter Topics
* Exactly Once Processing
* Transactional Messaging
