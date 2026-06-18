package com.mini.producerservice.service;

import com.mini.producerservice.model.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderEvent event) {

        kafkaTemplate.send(
                "orders-topic",
                event.getOrderId().toString(),
                event
        );

        System.out.println("Published Order: " + event.getOrderId());
    }
}