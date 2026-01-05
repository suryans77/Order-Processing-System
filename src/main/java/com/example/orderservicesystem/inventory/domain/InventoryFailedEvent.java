package com.example.orderservicesystem.inventory.domain;

import java.util.UUID;

public record InventoryFailedEvent(UUID orderId) {}

