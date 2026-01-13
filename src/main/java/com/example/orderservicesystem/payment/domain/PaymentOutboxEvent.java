package com.example.orderservicesystem.payment.domain;

import com.example.orderservicesystem.shared.domain.BaseOutboxEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "payment_outbox")
public class PaymentOutboxEvent extends BaseOutboxEvent {
    public PaymentOutboxEvent(UUID aggregateId, String eventType, String payload) {
        super("PAYMENT", aggregateId, eventType, payload);
    }
    protected PaymentOutboxEvent() {}

    public static PaymentOutboxEvent create_PaymentCompleted(UUID orderId, String payload) {
        return new PaymentOutboxEvent(orderId, "PAYMENT_COMPLETED", payload);
    }
}
