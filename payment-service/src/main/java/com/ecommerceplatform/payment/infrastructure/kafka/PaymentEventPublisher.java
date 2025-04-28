package com.ecommerceplatform.payment.infrastructure.kafka;

import com.ecommerceplatform.common.event.EventEnvelope;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                 ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void publish(String topic, String aggregateId, String eventType, T data) {
        EventEnvelope<T> envelope = new EventEnvelope<>(
                eventType,
                aggregateId,
                data,
                Instant.now()
        );

        try {
            String payload = objectMapper.writeValueAsString(envelope);
            kafkaTemplate.send(topic, aggregateId, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event envelope", e);
        }
    }
}