package com.example.orderservicesystem.inventory.domain;

import java.util.UUID;

/**
 * The response DTO sent from Inventory back to the Order Service.
 */
public record InventoryResultEvent(
        UUID orderId,
        boolean success,
        String failureReason
) {
    // Records automatically generate accessor methods:
    // Use .orderId() instead of .getOrderId()
    // Use .success() instead of .isSuccess()
}