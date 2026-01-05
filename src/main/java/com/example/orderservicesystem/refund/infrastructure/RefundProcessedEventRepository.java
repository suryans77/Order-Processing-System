package com.example.orderservicesystem.refund.infrastructure;

import com.example.orderservicesystem.inventory.domain.InventoryProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundProcessedEventRepository extends JpaRepository<InventoryProcessedEvent, String> {}

