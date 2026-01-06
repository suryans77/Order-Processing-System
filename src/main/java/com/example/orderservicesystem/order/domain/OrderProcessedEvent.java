package com.example.orderservicesystem.order.domain;

import com.example.orderservicesystem.shared.domain.BaseProcessedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "order_processed_events") // Own table!
public class OrderProcessedEvent extends BaseProcessedEvent {
    public OrderProcessedEvent(UUID id) { super(id); }
}