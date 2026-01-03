package com.example.orderservicesystem.inventory.application;

import com.example.orderservicesystem.shared.application.EventPublisher;
import com.example.orderservicesystem.payment.domain.PaymentOutboxEvent;
import com.example.orderservicesystem.payment.infrastructure.PaymentOutboxRepository;
import com.example.orderservicesystem.shared.application.AbstractOutboxPoller;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxPoller extends AbstractOutboxPoller<PaymentOutboxEvent> {
    public PaymentOutboxPoller(PaymentOutboxRepository repository, EventPublisher publisher) {
        super(repository, publisher, "PAYMENT");
    }

    @Scheduled(fixedDelay = 1000) // Payments are urgent! Runs every 1 second
    public void run() { super.poll(); }
}