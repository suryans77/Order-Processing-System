package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderCreatedEvent;
import com.example.orderservicesystem.order.domain.OrderOutboxEvent;
import com.example.orderservicesystem.order.infrastructure.OrderOutboxRepository;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    public CreateOrderService(
            OrderRepository orderRepository,
            OrderOutboxRepository orderOutboxRepository,
            ObjectMapper objectMapper
    ) {
        this.orderRepository = orderRepository;
        this.orderOutboxRepository = orderOutboxRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Idempotent order creation.
     * Same idempotencyKey => same Order returned.
     */
    @Transactional
    public Order createOrder(BigDecimal amount, String idempotencyKey) {

        return orderRepository
                .findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> createNewOrder(amount, idempotencyKey));
    }

    private Order createNewOrder(BigDecimal amount, String idempotencyKey) {

        // 1️⃣ Create + persist Order (source of truth)
        Order order = Order.create(amount, idempotencyKey);
        Order savedOrder = orderRepository.save(order);

        // 2️⃣ Create domain event
        OrderCreatedEvent domainEvent =
                new OrderCreatedEvent(savedOrder.getId());

        // 3️⃣ Serialize event payload
        String payload = serialize(domainEvent);

        // 4️⃣ Persist Outbox event (same transaction!)
        OrderOutboxEvent outboxEvent =
                OrderOutboxEvent.create_OrderCreatedEvent(
                        savedOrder.getId(),
                        payload
                );

        orderOutboxRepository.save(outboxEvent);

        return savedOrder;
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to serialize domain event", e
            );
        }
    }
}
