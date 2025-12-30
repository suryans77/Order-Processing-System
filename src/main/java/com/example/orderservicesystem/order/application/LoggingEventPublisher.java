package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingEventPublisher.class);

    @Override
    public void publish(OutboxEvent event) {
        log.info(
                "📤 Publishing event type={} aggregateId={} payload={}",
                event.getEventType(),
                event.getAggregateId(),
                event.getPayload()
        );
    }
}
