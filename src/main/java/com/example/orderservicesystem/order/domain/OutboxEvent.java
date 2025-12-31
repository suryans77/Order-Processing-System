package com.example.orderservicesystem.order.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    //order id
    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean processed;

    @Column(name = "processed_at")
    private Instant processedAt;

    protected OutboxEvent() {
        // for JPA
    }

    private OutboxEvent(UUID id,
                        String aggregateType,
                        UUID aggregateId,
                        String eventType,
                        String payload,
                        Instant createdAt) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.processed = false;
    }

    //Factory method
    public static OutboxEvent create(String aggregateType,
                                     UUID aggregateId,
                                     String eventType,
                                     String payload) {
        return new OutboxEvent(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                eventType,
                payload,
                Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public boolean isProcessed() {
        return processed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getEventType(){
        return eventType;
    }
    public String getPayload() {
        return payload;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void markProcessed() {
        this.processed = true;
        this.processedAt = Instant.now();
    }

    public String getAggregateType() {
        return aggregateType;
    }
}
