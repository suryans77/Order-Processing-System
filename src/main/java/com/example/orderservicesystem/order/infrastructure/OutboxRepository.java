package com.example.orderservicesystem.order.infrastructure;

import com.example.orderservicesystem.order.domain.OutboxEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByProcessedFalse(Pageable pageable);

}
