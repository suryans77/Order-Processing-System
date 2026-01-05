package com.example.orderservicesystem.inventory.domain;

import com.example.orderservicesystem.shared.domain.BaseProcessedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "inventory_processed_events")
public class InventoryProcessedEvent extends BaseProcessedEvent {
    public InventoryProcessedEvent(UUID id) { super(id); }
}
