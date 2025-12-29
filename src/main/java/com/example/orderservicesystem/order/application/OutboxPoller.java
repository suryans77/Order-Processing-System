package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.OutboxEvent;
import com.example.orderservicesystem.order.infrastructure.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxPoller {

    private static final Logger log = LoggerFactory.getLogger(OutboxPoller.class);
    private static final int BATCH_SIZE = 100;

    private final OutboxRepository outboxRepository;

    public OutboxPoller(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    /**
     * Poll unprocessed outbox events every 3 seconds
     */
    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void poll() {

        Pageable page = PageRequest.of(
                0,
                BATCH_SIZE,
                Sort.by("createdAt").ascending()
        );

        List<OutboxEvent> events =
                outboxRepository.findByProcessedFalse(page);

        if (events.isEmpty()) {
            return;
        }

        for (OutboxEvent event : events) {
            try {
                // For now, just log. Later -> publish to Kafka.
                log.info("📤 Outbox dispatch: type={}, aggregateId={}, payload={}",
                        event.getEventType(),
                        event.getAggregateId(),
                        event.getPayload());

                // Later:
                // kafkaTemplate.send("orders", event.getAggregateId().toString(), event.getPayload());

                event.markProcessed();

            } catch (Exception ex) {
                log.error("❌ Failed to dispatch outbox event id={}. Will retry.",
                        event.getId(), ex);
            }
        }

        // Persist only the processed flag updates
        outboxRepository.saveAll(events);
    }
}
