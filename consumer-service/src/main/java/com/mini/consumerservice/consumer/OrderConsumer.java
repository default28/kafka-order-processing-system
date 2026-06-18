package com.mini.consumerservice.consumer;

import com.mini.consumerservice.entity.Order;
import com.mini.consumerservice.model.OrderEvent;
import com.mini.consumerservice.repository.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final OrderRepository repository;

    public OrderConsumer(
            OrderRepository repository) {
        this.repository = repository;
    }

//    @RetryableTopic(
//            attempts = "3",
//            dltTopicSuffix = "-dlt"
//    )
    @KafkaListener(
            topics = "orders-topic",
            groupId = "order-group"
    )
    public void consume(OrderEvent event,
                        ConsumerRecord<String, OrderEvent> record) {
        try {
            System.out.println("================================");

            System.out.println("Order Received");
            System.out.println("Order Id : " + event.getOrderId());
            System.out.println("Customer : " + event.getCustomer());
            System.out.println("Amount : " + event.getAmount());
            System.out.println("================================");

            System.out.println(
                    "Partition: " + record.partition());

            System.out.println(
                    "Offset: " + record.offset());

            System.out.println(
                    "OrderId: " + event.getOrderId());

            Order order = new Order();

            order.setOrderId(event.getOrderId());
            order.setCustomer(event.getCustomer());
            order.setAmount(event.getAmount());

        if(event.getOrderId() == 99) {

            throw new RuntimeException(
                    "Simulated Failure");
        }

            if (repository.existsById(event.getOrderId())) {

                System.out.println(
                        "Duplicate Order Ignored: "
                                + event.getOrderId());

                return;
            }

            repository.save(order);


            System.out.println(
                    "Saved Order : "
                            + event.getOrderId());
        } catch(Exception ex) {
            System.out.println("failed order: " + event.getOrderId());
            ex.printStackTrace();
        }
    }
}



//@Service
//public class OrderConsumer {
//
//    @KafkaListener(
//            topics = "orders-topic",
//            groupId = "order-group"
//    )
//    public void consume(OrderEvent event) {
//
//        System.out.println("================================");
//
//        System.out.println("Order Received");
//
//        System.out.println("Order Id : " + event.getOrderId());
//
//        System.out.println("Customer : " + event.getCustomer());
//
//        System.out.println("Amount : " + event.getAmount());
//
//        System.out.println("================================");
//    }
//}