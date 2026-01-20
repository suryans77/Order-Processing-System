package com.example.orderservicesystem.refund.infrastructure;

import com.example.orderservicesystem.inventory.domain.InventoryProcessedEvent;
import com.example.orderservicesystem.refund.domain.RefundProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefundProcessedEventRepository extends JpaRepository<RefundProcessedEvent, UUID> {}

