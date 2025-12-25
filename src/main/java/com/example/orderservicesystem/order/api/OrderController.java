package com.example.orderservicesystem.order.api;

import com.example.orderservicesystem.order.application.CreateOrderService;
import com.example.orderservicesystem.order.application.GetOrderService;
import com.example.orderservicesystem.order.domain.Order;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderService createOrderService;
    private final GetOrderService getOrderService;

    public OrderController(CreateOrderService createOrderService,
                           GetOrderService getOrderService) {
        this.createOrderService = createOrderService;
        this.getOrderService = getOrderService;
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

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        Order order = getOrderService.getById(id);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

}
