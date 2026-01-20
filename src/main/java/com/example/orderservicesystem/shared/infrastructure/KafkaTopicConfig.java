package com.example.orderservicesystem.shared.infrastructure;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderEvents() {
        return TopicBuilder.name("order-events")
                .partitions(3)
                .replicas(1) // Use 3 in production
                .build();
    }

    @Bean
    public NewTopic paymentEvents() {
        return TopicBuilder.name("payment-events")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic inventoryEvents() {
        return TopicBuilder.name("inventory-events")
                .partitions(3)
                .build();
    }
}
