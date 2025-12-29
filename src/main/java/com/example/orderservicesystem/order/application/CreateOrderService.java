package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderCreatedEvent;
import com.example.orderservicesystem.order.domain.OutboxEvent;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import com.example.orderservicesystem.order.infrastructure.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public CreateOrderService(OrderRepository orderRepository,
                              OutboxRepository outboxRepository,
                              ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Order createOrder(BigDecimal amount, String idempotencyKey) {

        return orderRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> createNewOrder(amount, idempotencyKey));
    }

    private Order createNewOrder(BigDecimal amount, String idempotencyKey) {

        // 1️⃣ Save Order
        Order order = Order.create(amount, idempotencyKey);
        Order saved = orderRepository.save(order);

        // 2️⃣ Build domain event + payload
        OrderCreatedEvent event = new OrderCreatedEvent(saved.getId());
        String payload = serialize(event);

        // 3️⃣ Save Outbox event in SAME TX
        OutboxEvent outbox = OutboxEvent.create(
                "Order",
                saved.getId(),
                "ORDER_CREATED",
                payload
        );

        outboxRepository.save(outbox);

        return saved;
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize event", e);
        }
    }
}
