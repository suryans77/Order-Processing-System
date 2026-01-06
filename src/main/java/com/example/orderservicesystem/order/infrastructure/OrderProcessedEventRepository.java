package com.example.orderservicesystem.order.infrastructure;

import com.example.orderservicesystem.inventory.domain.InventoryProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProcessedEventRepository extends JpaRepository<InventoryProcessedEvent, String> {}


