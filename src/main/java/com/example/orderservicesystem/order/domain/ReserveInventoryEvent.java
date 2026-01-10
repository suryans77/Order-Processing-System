package com.example.orderservicesystem.order.domain;

import java.util.UUID;

/**
 * Represents the command to start inventory reservation.
 */
public record ReserveInventoryEvent(UUID orderId) {
}