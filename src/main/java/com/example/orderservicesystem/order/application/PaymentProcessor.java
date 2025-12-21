package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderCreatedEvent;
import com.example.orderservicesystem.order.domain.OrderStatus;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentProcessor {

    private final OrderRepository orderRepository;

    public PaymentProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            // simulate external call
            Thread.sleep(7000);

            Order order = orderRepository.findById(event.orderId())
                    .orElseThrow();

            order.markPaymentPending();
            order.markPaid();

            orderRepository.save(order);

            System.out.println("Payment processed for order " + order.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
