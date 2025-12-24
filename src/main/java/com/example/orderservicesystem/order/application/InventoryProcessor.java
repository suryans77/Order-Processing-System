package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.InventoryFailedEvent;
import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.PaymentCompletedEvent;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class InventoryProcessor {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public InventoryProcessor(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {

        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        try {
            Thread.sleep(5000);

            Order order = orderRepository.findById(event.orderId())
                    .orElseThrow();

            // simulate 30% failure
            if (Math.random() < 0.3) {
                order.markInventoryFailed();
                orderRepository.save(order);

                eventPublisher.publishEvent(
                        new InventoryFailedEvent(order.getId())
                );

                System.out.println("Inventory FAILED for order " + order.getId());
                return;
            }

            order.reserveInventory();
            order.complete();

            orderRepository.save(order);

            System.out.println("Inventory reserved & order completed for " + order.getId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
