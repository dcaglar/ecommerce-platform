package com.ecommerceplatform.payment.infrastructure.outbox;

import com.ecommerceplatform.common.event.OutboxEvent;
import com.ecommerceplatform.payment.infrastructure.kafka.PaymentEventPublisher;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxPublisherService {

    private final OutboxEventRepository outboxEventRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ObjectMapper objectMapper;

    public OutboxPublisherService(OutboxEventRepository outboxEventRepository,
                                  PaymentEventPublisher paymentEventPublisher,
                                  ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.paymentEventPublisher = paymentEventPublisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "outbox-notify", groupId = "outbox-publisher-group")
    @Transactional
    public void handleOutboxNotification(String message) {
        Long outboxId = Long.valueOf(message);

        OutboxEvent event = outboxEventRepository.findById(outboxId)
                .orElseThrow(() -> new RuntimeException("OutboxEvent not found with id: " + outboxId));

        try {
            // Deserialize payload into event object (generic JSON Node or object)
            Object payloadObject = objectMapper.readTree(event.getPayload());

            // Publish the event (topic = eventType)
            paymentEventPublisher.publish(
                    event.getEventType(),         // Kafka topic
                    event.getAggregateId(),       // Key
                    event.getEventType(),         // eventType inside envelope
                    payloadObject                 // Deserialized business event
            );

            // Mark event as PUBLISHED
            OutboxEvent publishedEvent = event.markAsPublished();
            outboxEventRepository.save(publishedEvent);

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish outbox event", e);
        }
    }
}