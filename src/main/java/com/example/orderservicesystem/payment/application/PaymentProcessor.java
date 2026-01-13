package com.example.orderservicesystem.payment.application;

import com.example.orderservicesystem.order.domain.OrderCreatedEvent;
import com.example.orderservicesystem.payment.domain.PaymentCompletedEvent;
import com.example.orderservicesystem.payment.domain.PaymentOutboxEvent;
import com.example.orderservicesystem.payment.domain.PaymentProcessedEvent;
import com.example.orderservicesystem.payment.infrastructure.PaymentOutboxRepository;
import com.example.orderservicesystem.payment.infrastructure.PaymentProcessedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class PaymentProcessor {

    private final PaymentOutboxRepository outboxRepository;
    private final PaymentProcessedEventRepository processedRepository;
    private final ObjectMapper objectMapper;

    public PaymentProcessor(PaymentOutboxRepository outboxRepository,
                            PaymentProcessedEventRepository processedRepository,
                            ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.processedRepository = processedRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "order-events", groupId = "payment-service-group")
    public void onOrderCreated(String message, @Header(KafkaHeaders.RECEIVED_KEY) UUID eventId) {
        // 1. Idempotency Check
        if (processedRepository.existsById(eventId)) return;

        try {
            // 2. Deserialize the incoming Order event
            OrderCreatedEvent orderEvent = objectMapper.readValue(message, OrderCreatedEvent.class);

            // 3. Business Logic: Simulate charging the customer
            // In a real app, you'd call Stripe/PayPal here.

            // 4. Prepare the Response
            PaymentCompletedEvent response = new PaymentCompletedEvent(orderEvent.orderId());
            String payload = objectMapper.writeValueAsString(response);

            // 5. Save to Payment's Outbox
            PaymentOutboxEvent outboxEvent = PaymentOutboxEvent.create_PaymentCompleted(
                    orderEvent.orderId(),
                    payload
            );
            outboxRepository.save(outboxEvent);

            // 6. Record that this event was processed
            processedRepository.save(new PaymentProcessedEvent(eventId));

        } catch (Exception e) {
            throw new RuntimeException("Payment processing failed", e);
        }
    }
}
