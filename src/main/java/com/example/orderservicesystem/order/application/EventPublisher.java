package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.OutboxEvent;

public interface EventPublisher {
    void publish(OutboxEvent event);
}

