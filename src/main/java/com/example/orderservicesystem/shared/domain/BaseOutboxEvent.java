package com.example.orderservicesystem.shared.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseOutboxEvent {

    @Id
    @Column(nullable = false, updatable = false)
    protected UUID id; // evenID

    @Column(nullable = false)
    protected String aggregateType;

    @Column(nullable = false)
    protected UUID aggregateId; //orderID

    @Column(nullable = false)
    protected String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    protected String payload;

    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @Column(nullable = false)
    protected boolean processed;

    protected Instant processedAt;

    protected BaseOutboxEvent() {
        // Required by JPA
    }

    protected BaseOutboxEvent(
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload
    ) {
        this.id = UUID.randomUUID();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = Instant.now();
        this.processed = false;
    }

    /* -----------------
       Domain behavior
       ----------------- */

    public void markProcessed() {
        this.processed = true;
        this.processedAt = Instant.now();
    }

    /* -----------------
       Getters (no setters)
       ----------------- */

    public UUID getId() {
        return id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}
