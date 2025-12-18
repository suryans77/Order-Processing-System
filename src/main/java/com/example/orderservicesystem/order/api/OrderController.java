package com.example.orderservicesystem.order.api;

import com.example.orderservicesystem.order.application.CreateOrderService;
import com.example.orderservicesystem.order.domain.Order;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderService createOrderService;

    public OrderController(CreateOrderService createOrderService) {
        this.createOrderService = createOrderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        Order order = createOrderService.createOrder(
                request.amount(),
                idempotencyKey
        );

        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
