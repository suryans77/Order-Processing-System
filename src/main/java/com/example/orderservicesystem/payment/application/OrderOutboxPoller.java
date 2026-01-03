package com.example.orderservicesystem.payment.application;

import com.example.orderservicesystem.shared.application.EventPublisher;
import com.example.orderservicesystem.order.domain.OrderOutboxEvent;
import com.example.orderservicesystem.shared.application.AbstractOutboxPoller;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxPoller extends AbstractOutboxPoller<OrderOutboxEvent> {
    public OrderOutboxPoller(OrderOutboxRepository repository, EventPublisher publisher) {
        super(repository, publisher, "ORDER");
    }

    @Scheduled(fixedDelay = 2000) // Runs every 2 seconds
    public void run() { super.poll(); }
}
