package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderOutboxEvent;
import com.example.orderservicesystem.order.domain.OrderProcessedEvent;
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

    @Transactional
    @KafkaListener(topics = "inventory-events", groupId = "order-service-group")
    public void onInventoryResult(String message, @Header("kafka_messageId") String eventId) {
        // 1. Idempotency Check
        if (processedRepository.existsById(UUID.fromString(eventId))) return;

        try {
            InventoryResultEvent event = objectMapper.readValue(message, InventoryResultEvent.class);
            Order order = orderRepository.findById(event.getOrderId()).orElseThrow();

            if (event.isSuccess()) {
                // SUCCESS FLOW
                order.complete();
            } else {
                // FAILURE FLOW: Trigger Refund
                order.markInventoryFailed();
                String payload = objectMapper.writeValueAsString(new RefundRequestEvent(order.getId()));
                outboxRepository.save(OrderOutboxEvent.create_RefundRequestEvent(order.getId(), payload));
            }

            orderRepository.save(order);
            processedRepository.save(new OrderProcessedEvent(UUID.fromString(eventId)));

        } catch (Exception e) {
            throw new RuntimeException("Failed to process inventory response", e);
        }
    }
}
