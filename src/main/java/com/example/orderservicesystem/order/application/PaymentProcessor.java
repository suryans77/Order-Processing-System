package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderCreatedEvent;
import com.example.orderservicesystem.order.domain.OrderStatus;
import com.example.orderservicesystem.order.domain.PaymentCompletedEvent;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentProcessor {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentProcessor(OrderRepository orderRepository,
                            ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            Thread.sleep(5000);

            Order order = orderRepository.findById(event.orderId())
                    .orElseThrow();

            order.markPaymentPending();
            order.markPaid();

            orderRepository.save(order);

            eventPublisher.publishEvent(
                    new PaymentCompletedEvent(order.getId())
            );

            System.out.println("Payment completed for order " + order.getId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
