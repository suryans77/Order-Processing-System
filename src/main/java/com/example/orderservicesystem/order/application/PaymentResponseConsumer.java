package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.*;
import com.example.orderservicesystem.order.infrastructure.*;
import com.example.orderservicesystem.payment.domain.PaymentCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
public class PaymentResponseConsumer {

    private final OrderRepository orderRepository;
    private final OrderProcessedEventRepository processedRepository;
    private final OrderOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public PaymentResponseConsumer(OrderRepository orderRepository,
                                   OrderProcessedEventRepository processedRepository,
                                   OrderOutboxRepository outboxRepository,
                                   ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.processedRepository = processedRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "payment-events", groupId = "order-service-group")
    public void onPaymentResult(String message, @Header("kafka_messageId") UUID eventId) {
        // 1. Idempotency Check - Use the UUID directly
        if (processedRepository.existsById(eventId)) return;

        try {
            // 2. Business Logic: Update Order status
            PaymentCompletedEvent event = objectMapper.readValue(message, PaymentCompletedEvent.class);
            Order order = orderRepository.findById(event.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            order.markPaid(); // Updates status to PAID
            orderRepository.save(order);

            // 3. Trigger Next Step: Inventory
            // Ensure ReserveInventoryEvent is defined in your domain
            String payload = objectMapper.writeValueAsString(new ReserveInventoryEvent(order.getId()));
            OrderOutboxEvent nextStep = OrderOutboxEvent.create_ReserveInventoryEvent(order.getId(), payload);
            outboxRepository.save(nextStep);

            // 4. Mark processed - Pass the UUID directly to your constructor
            processedRepository.save(new OrderProcessedEvent(eventId));

        } catch (Exception e) {
            throw new RuntimeException("Failed to process payment response", e);
        }
    }
}