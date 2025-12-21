package com.example.orderservicesystem.order.application;

import com.example.orderservicesystem.order.domain.Order;
import com.example.orderservicesystem.order.domain.OrderCreatedEvent;
import com.example.orderservicesystem.order.infrastructure.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateOrderService(OrderRepository orderRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order createOrder(BigDecimal amount, String idempotencyKey) {

        return orderRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> {
                    Order order = Order.create(amount, idempotencyKey);
                    Order saved = orderRepository.save(order);

                    eventPublisher.publishEvent(
                            new OrderCreatedEvent(saved.getId())
                    );

                    return saved;
                });
    }
}
