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
    private static final int BATCH_SIZE = 50;

    private final OutboxRepository outboxRepository;
    private final EventPublisher eventPublisher;

    public OutboxPoller(OutboxRepository outboxRepository, EventPublisher eventPublisher) {
        this.outboxRepository = outboxRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 2500)
    @Transactional // Ensures the whole batch succeeds or fails together
    public void poll() {
        Pageable page = PageRequest.of(0, BATCH_SIZE, Sort.by("createdAt").ascending());

        List<OutboxEvent> events = outboxRepository.findByProcessedFalse(page);

        if (events.isEmpty()) {
            return;
        }

        log.info("Starting to poll {} unprocessed events", events.size());

        for (OutboxEvent event : events) {
            try {
                // If publish fails, it throws an exception and markProcessed() is skipped
                eventPublisher.publish(event);

                // Only called if publish was successful
                event.markProcessed();

            } catch (Exception e) {
                // Error is caught here so the rest of the 50 events can still try to publish
                log.error("❌ Skipping event id={} due to failure. Will retry in next cycle.", event.getId());
            }
        }

        // Finalize all state changes in the database
        outboxRepository.saveAll(events);
    }
}