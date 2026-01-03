package com.example.orderservicesystem.shared.application;

public interface EventPublisher {
    void publish(OutboxEvent event);
}

