package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.inventory.domain.InventoryResultEvent;
import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderOutboxEvent;
import com.example.orderservicesystem.order.domain.OrderProcessedEvent;
import com.example.orderservicesystem.order.domain.RefundRequestEvent;
import com.example.orderservicesystem.order.infrastructure.OrderOutboxRepository;
import com.example.orderservicesystem.order.infrastructure.OrderProcessedEventRepository;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.handler.annotation.Header;

import java.util.UUID;

@Component
public class InventoryResponseConsumer {

    private final OrderRepository orderRepository;
    private final OrderProcessedEventRepository processedRepository;
    private final OrderOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public InventoryResponseConsumer(OrderRepository orderRepository,
                                     OrderProcessedEventRepository processedRepository,
                                     OrderOutboxRepository outboxRepository,
                                     ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.processedRepository = processedRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "inventory-events", groupId = "order-service-group")
    public void onInventoryResult(String message, @Header("kafka_messageId") UUID eventId) {
        // 1. Idempotency Check
        if (processedRepository.existsById(eventId)) return;

        try {
            InventoryResultEvent event = objectMapper.readValue(message, InventoryResultEvent.class);
            Order order = orderRepository.findById(event.orderId()).orElseThrow(() -> new RuntimeException("Order not found"));

            if (event.success()) {
                // SUCCESS FLOW
                order.complete();
            } else {
                // FAILURE FLOW: Trigger Refund
                order.markInventoryFailed();
                String payload = objectMapper.writeValueAsString(new RefundRequestEvent(order.getId()));
                outboxRepository.save(OrderOutboxEvent.create_RefundRequestEvent(order.getId(), payload));
            }

            orderRepository.save(order);
            processedRepository.save(new OrderProcessedEvent(eventId));

        } catch (Exception e) {
            throw new RuntimeException("Failed to process inventory response", e);
        }
    }
}
