package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.InventoryFailedEvent;
import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class RefundProcessor {

    private final OrderRepository orderRepository;

    public RefundProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleInventoryFailed(InventoryFailedEvent event) {
        try {
            Thread.sleep(1500);

            Order order = orderRepository.findById(event.orderId())
                    .orElseThrow();

            order.refund();
            orderRepository.save(order);

            System.out.println("Refund processed for order " + order.getId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

