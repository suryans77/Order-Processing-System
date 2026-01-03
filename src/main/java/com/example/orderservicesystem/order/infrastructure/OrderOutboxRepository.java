package com.example.orderservicesystem.order.infrastructure;

import com.example.orderservicesystem.order.domain.OrderOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface OrderOutboxRepository extends JpaRepository<OrderOutboxEvent, UUID> {}
