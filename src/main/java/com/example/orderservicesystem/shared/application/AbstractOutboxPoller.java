package com.example.orderservicesystem.shared.application;

import com.example.orderservicesystem.shared.domain.BaseOutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public abstract class AbstractOutboxPoller<T extends BaseOutboxEvent> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JpaRepository<T, java.util.UUID> repository;
    private final EventPublisher eventPublisher;
    private final String serviceName;

    protected AbstractOutboxPoller(JpaRepository<T, java.util.UUID> repository,
                                   EventPublisher eventPublisher,
                                   String serviceName) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.serviceName = serviceName;
    }

    @Transactional
    public void poll() {
        // Fetch 50 unprocessed events for this specific service
        List<T> events = repository.findAll(
                        PageRequest.of(0, 50, Sort.by("createdAt").ascending())
                ).getContent().stream()
                .filter(e -> !e.isProcessed())
                .toList();

        if (events.isEmpty()) return;

        log.info("Polling {} events for service: {}", events.size(), serviceName);

        for (T event : events) {
            try {
                // Publish to Kafka (this is the synchronous call with .get())
                eventPublisher.publish(event);

                // Mark as processed in the specific table
                event.markProcessed();
            } catch (Exception e) {
                log.error("Failed to publish {} event {}: {}", serviceName, event.getId(), e.getMessage());
                // Event remains unprocessed = false to be retried next time
            }
        }
        repository.saveAll(events);
    }
}