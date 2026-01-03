package com.example.orderservicesystem.refund.application;

import com.example.orderservicesystem.shared.application.EventPublisher;
import com.example.orderservicesystem.inventory.domain.InventoryOutboxEvent;
import com.example.orderservicesystem.inventory.infrastructure.InventoryOutboxRepository;
import com.example.orderservicesystem.shared.application.AbstractOutboxPoller;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InventoryOutboxPoller extends AbstractOutboxPoller<InventoryOutboxEvent> {

    public InventoryOutboxPoller(
            InventoryOutboxRepository repository,
            EventPublisher eventPublisher
    ) {
        // Pass dependencies and the service name to the generic base class
        super(repository, eventPublisher, "INVENTORY");
    }

    @Scheduled(fixedDelay = 3000) // Polls every 3 seconds
    public void run() {
        super.poll();
    }
}
