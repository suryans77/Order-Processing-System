package com.example.orderservicesystem.inventory.domain;

import com.example.orderservicesystem.shared.domain.BaseOutboxEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "inventory_outbox")
public class InventoryOutboxEvent extends BaseOutboxEvent {
    public InventoryOutboxEvent(UUID aggregateId, String eventType, String payload) {
        super("INVENTORY", aggregateId, eventType, payload);
    }
    protected InventoryOutboxEvent() {}
}
