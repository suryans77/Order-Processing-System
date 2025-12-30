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

    public OutboxPoller(OutboxRepository outboxRepository,
                        EventPublisher eventPublisher) {
        this.outboxRepository = outboxRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 2500)
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
                eventPublisher.publish(event);
                event.markProcessed();
            } catch (Exception e) {
                log.error(
                        "❌ Failed to publish outbox event id={}",
                        event.getId(),
                        e
                );
            }
        }
    }
}
