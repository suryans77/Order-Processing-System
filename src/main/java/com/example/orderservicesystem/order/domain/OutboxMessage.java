package com.example.orderservicesystem.order.domain;

public record OutboxMessage(
        java.util.UUID eventId,
        String aggregateType,
        java.util.UUID aggregateId,
        String eventType,
        String payload,
        java.time.Instant createdAt
) {}
