package com.example.orderservicesystem.order.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotNull
        @Positive
        BigDecimal amount
) {}
