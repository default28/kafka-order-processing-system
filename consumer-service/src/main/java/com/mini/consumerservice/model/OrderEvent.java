package com.mini.consumerservice.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderEvent {

    private Long orderId;
    private String customer;
    private Double amount;

    // getters setters

}