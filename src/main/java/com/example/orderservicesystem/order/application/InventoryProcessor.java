package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.PaymentCompletedEvent;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InventoryProcessor {

    private final OrderRepository orderRepository;

    public InventoryProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        try {
            Thread.sleep(2000);

            Order order = orderRepository.findById(event.orderId())
                    .orElseThrow();

            order.reserveInventory();
            order.complete();

            orderRepository.save(order);

            System.out.println("Inventory reserved & order completed for " + order.getId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
