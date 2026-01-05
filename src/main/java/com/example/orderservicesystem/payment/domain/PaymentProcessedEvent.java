package com.example.orderservicesystem.payment.domain;

import com.example.orderservicesystem.shared.domain.BaseProcessedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "payment_processed_events") // Own table!
public class PaymentProcessedEvent extends BaseProcessedEvent {
    public PaymentProcessedEvent(UUID id) { super(id); }
}