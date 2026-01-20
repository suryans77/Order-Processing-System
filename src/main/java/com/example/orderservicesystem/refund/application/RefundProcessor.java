package com.example.orderservicesystem.refund.application;

import com.example.orderservicesystem.order.domain.RefundRequestEvent;
import com.example.orderservicesystem.refund.domain.RefundProcessedEvent;
import com.example.orderservicesystem.refund.infrastructure.RefundProcessedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class RefundProcessor {

    private final RefundProcessedEventRepository processedRepository;
    private final ObjectMapper objectMapper;

    public RefundProcessor(RefundProcessedEventRepository processedRepository,
                           ObjectMapper objectMapper) {
        this.processedRepository = processedRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "order-events", groupId = "refund-service-group")
    public void onRefundRequest(String message, @Header(KafkaHeaders.RECEIVED_KEY) UUID eventId) {
        // 1. Idempotency Check
        if (processedRepository.existsById(eventId)) return;

        try {
            // 2. Deserialize the request
            RefundRequestEvent request = objectMapper.readValue(message, RefundRequestEvent.class);

            // 3. Business Logic: Perform the Refund (Simulated)
            System.out.println("Refunding money for Order: " + request.orderId());

            // 4. Mark as processed
            processedRepository.save(new RefundProcessedEvent(eventId));

        } catch (Exception e) {
            throw new RuntimeException("Refund processing failed", e);
        }
    }
}
