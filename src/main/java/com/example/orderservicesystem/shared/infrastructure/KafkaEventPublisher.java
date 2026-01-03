package com.example.orderservicesystem.shared.infrastructure;

import com.example.orderservicesystem.shared.application.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(OutboxEvent event) {
        try {
            // 1. Map to DTO
            OutboxMessage message = new OutboxMessage(
                    event.getId(),
                    event.getAggregateType(),
                    event.getAggregateId(),
                    event.getEventType(),
                    event.getPayload(),
                    event.getCreatedAt()
            );

            // 2. Serialize
            String jsonPayload = objectMapper.writeValueAsString(message);

            // 3. Synchronous Send
            // We use .get() to wait for Kafka's acknowledgment.
            // If Kafka is down, this throws an exception, preventing the Poller from marking it 'processed'.
            kafkaTemplate.send(
                    "order-events",                  // Topic
                    event.getAggregateId().toString(), // Key (Ensures ordering)
                    jsonPayload                      // Value
            ).get();

            log.info("✅ Successfully published event {} to Kafka topic 'order-events'", event.getId());

        } catch (Exception e) {
            log.error("❌ Kafka publishing failed for event {}: {}", event.getId(), e.getMessage());
            // Re-throw so the OutboxPoller knows NOT to call event.markProcessed()
            throw new RuntimeException("Kafka publish failed", e);
        }
    }
}