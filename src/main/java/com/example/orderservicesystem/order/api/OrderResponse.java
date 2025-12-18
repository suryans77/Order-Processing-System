package com.example.orderservicesystem.order.api;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderStatus;

import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        OrderStatus status
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getStatus());
    }
}
