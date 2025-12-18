package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;

    public CreateOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(BigDecimal amount, String idempotencyKey) {

        return orderRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> {
                    Order order = Order.create(amount, idempotencyKey);
                    return orderRepository.save(order);
                });
    }
}
