package com.ecommerceplatform.common.event;

import java.time.LocalDateTime;
import java.util.Objects;

public class OutboxEvent {

    private final Long id; // nullable for new
    private final String eventType;
    private final String aggregateId;
    private final String payload;
    private final String status; // NEW, PUBLISHED
    private final LocalDateTime createdAt;

    public OutboxEvent(Long id, String eventType, String aggregateId, String payload, String status, LocalDateTime createdAt) {
        this.id = id;
        this.eventType = Objects.requireNonNull(eventType);
        this.aggregateId = Objects.requireNonNull(aggregateId);
        this.payload = Objects.requireNonNull(payload);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public Long getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getPayload() {
        return payload;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OutboxEvent markAsPublished() {
        return new OutboxEvent(this.id, this.eventType, this.aggregateId, this.payload, "PUBLISHED", this.createdAt);
    }
}