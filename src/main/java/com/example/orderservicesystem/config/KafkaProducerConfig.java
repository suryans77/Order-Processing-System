package com.example.orderservicesystem.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer; // New Import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<UUID, String> producerFactory() { // Changed key type to UUID
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class, // Changed to UUIDSerializer
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.ACKS_CONFIG, "all"
                )
        );
    }

    @Bean
    public KafkaTemplate<UUID, String> kafkaTemplate() { // Changed key type to UUID
        return new KafkaTemplate<>(producerFactory());
    }
}