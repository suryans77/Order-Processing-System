package com.example.orderservicesystem.order.domain;
import com.example.orderservicesystem.shared.domain.BaseOutboxEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "order_outbox")
public class OrderOutboxEvent extends BaseOutboxEvent {

    protected OrderOutboxEvent() {}

    private OrderOutboxEvent(
            UUID aggregateId,
            String eventType,
            String payload
    ) {
        super("Order", aggregateId, eventType, payload);
    }

    public static OrderOutboxEvent create_OrderCreatedEvent(
            UUID orderId,
            String payload
    ) {
        return new OrderOutboxEvent(
                orderId,
                "ORDER_CREATED",
                payload
        );
    }
}

