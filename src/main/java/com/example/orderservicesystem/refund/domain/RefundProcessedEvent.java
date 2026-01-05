package com.example.orderservicesystem.refund.domain;

import com.example.orderservicesystem.shared.domain.BaseProcessedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "refund_processed_events") // Own table!
public class RefundProcessedEvent extends BaseProcessedEvent {
    public RefundProcessedEvent(UUID id) { super(id); }
}