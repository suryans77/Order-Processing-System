package com.example.orderservicesystem.payment.infrastructure;

import com.example.orderservicesystem.payment.domain.PaymentOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxEvent, UUID> {}
