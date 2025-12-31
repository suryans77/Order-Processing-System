package com.example.orderservicesystem.order.infrastructure;

import com.example.orderservicesystem.order.domain.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, UUID> {
}

