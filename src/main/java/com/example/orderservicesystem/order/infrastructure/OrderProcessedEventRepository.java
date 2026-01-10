package com.example.orderservicesystem.order.infrastructure;

import com.example.orderservicesystem.inventory.domain.InventoryProcessedEvent;
import com.example.orderservicesystem.order.domain.OrderProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProcessedEventRepository extends JpaRepository<OrderProcessedEvent, UUID> {}


