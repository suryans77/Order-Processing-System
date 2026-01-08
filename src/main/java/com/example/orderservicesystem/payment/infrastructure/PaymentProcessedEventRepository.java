package com.example.orderservicesystem.payment.infrastructure;

import com.example.orderservicesystem.inventory.domain.InventoryProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentProcessedEventRepository extends JpaRepository<InventoryProcessedEvent, UUID> {}
