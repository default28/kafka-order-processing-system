package com.mini.producerservice.controller;

import com.mini.producerservice.model.OrderEvent;
import com.mini.producerservice.service.OrderProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer producer;

    public OrderController(OrderProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String createOrder(
            @RequestBody OrderEvent order) {

        producer.send(order);

        return "Order Published";
    }
}