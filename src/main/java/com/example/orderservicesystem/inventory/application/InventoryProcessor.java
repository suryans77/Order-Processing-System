package com.example.orderservicesystem.inventory.application;

import com.example.orderservicesystem.inventory.domain.*;
import com.example.orderservicesystem.inventory.infrastructure.*;
import com.example.orderservicesystem.order.domain.ReserveInventoryEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class InventoryProcessor {

    private final InventoryOutboxRepository outboxRepository;
    private final InventoryProcessedEventRepository processedRepository;
    private final ObjectMapper objectMapper;

    public InventoryProcessor(InventoryOutboxRepository outboxRepository,
                              InventoryProcessedEventRepository processedRepository,
                              ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.processedRepository = processedRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "order-events", groupId = "inventory-service-group")
    public void onReserveInventory(String message, @Header(KafkaHeaders.RECEIVED_KEY) UUID eventId) {
        // 1. Idempotency Check
        if (processedRepository.existsById(eventId)) return;

        try {
            // 2. Deserialize the command from the Order module
            ReserveInventoryEvent command = objectMapper.readValue(message, ReserveInventoryEvent.class);

            // 3. Business Logic: Check stock (Simulated)
            // In a real app, you would decrement stock in your database here.
            boolean isStockAvailable = true;

            // 4. Prepare the Result
            InventoryResultEvent result = new InventoryResultEvent(
                    command.orderId(),
                    isStockAvailable,
                    isStockAvailable ? null : "OUT_OF_STOCK"
            );
            String payload = objectMapper.writeValueAsString(result);

            // 5. Save to Inventory Outbox
            InventoryOutboxEvent outboxEvent = InventoryOutboxEvent.create_InventoryResult(
                    command.orderId(),
                    payload
            );
            outboxRepository.save(outboxEvent);

            // 6. Mark as processed
            processedRepository.save(new InventoryProcessedEvent(eventId));

        } catch (Exception e) {
            throw new RuntimeException("Inventory processing failed", e);
        }
    }
}
