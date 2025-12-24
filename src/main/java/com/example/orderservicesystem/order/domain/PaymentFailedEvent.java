package com.example.orderservicesystem.order.domain;

import java.util.UUID;

public record PaymentFailedEvent(UUID orderId) {}

