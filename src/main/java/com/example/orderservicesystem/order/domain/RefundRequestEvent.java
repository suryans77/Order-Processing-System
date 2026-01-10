package com.example.orderservicesystem.order.domain;

import java.util.UUID;

public record RefundRequestEvent(UUID orderId) {
}
