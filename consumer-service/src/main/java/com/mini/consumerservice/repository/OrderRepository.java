package com.mini.consumerservice.repository;

import com.mini.consumerservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<Order, Long> {
}