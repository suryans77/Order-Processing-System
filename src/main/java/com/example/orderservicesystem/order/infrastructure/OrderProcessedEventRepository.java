package com.example.orderservicesystem.order.infrastructure;

import com.example.orderservicesystem.inventory.domain.InventoryProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProcessedEventRepository extends JpaRepository<InventoryProcessedEvent, UUID> {}


