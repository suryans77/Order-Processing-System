package com.example.orderservicesystem.payment.domain;

import java.util.UUID;

public record PaymentFailedEvent(UUID orderId) {}

