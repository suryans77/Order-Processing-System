package com.example.orderservicesystem.order.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_order_idempotency", columnNames = "idempotencyKey")
        }
)
public class Order {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private String idempotencyKey;

    protected Order() {}

    private Order(UUID id, BigDecimal amount, String idempotencyKey) {
        this.id = id;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
        this.status = OrderStatus.CREATED;
    }

    public static Order create(BigDecimal amount, String idempotencyKey) {
        return new Order(UUID.randomUUID(), amount, idempotencyKey);
    }

    public UUID getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void markPaymentPending() {
        this.status = OrderStatus.PAYMENT_PENDING;
    }

    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    public void reserveInventory() {
        this.status = OrderStatus.INVENTORY_RESERVED;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

}
