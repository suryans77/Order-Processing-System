package com.example.orderservicesystem.shared.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseProcessedEvent {
    @Id
    protected UUID eventId;
    protected Instant processedAt;

    protected BaseProcessedEvent() {}
    public BaseProcessedEvent(UUID eventId) {
        this.eventId = eventId;
        this.processedAt = Instant.now();
    }
}
