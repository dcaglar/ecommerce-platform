package com.ecommerceplatform.common.event;

import java.time.Instant;
import java.util.Objects;

public class EventEnvelope<T> {
    private final String eventType;
    private final String aggregateId;
    private final T data;
    private final Instant createdAt;

    public EventEnvelope(String eventType, String aggregateId, T data, Instant createdAt) {
        this.eventType = Objects.requireNonNull(eventType);
        this.aggregateId = Objects.requireNonNull(aggregateId);
        this.data = Objects.requireNonNull(data);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    // Getters

    public String getEventType() {
        return eventType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public T getData() {
        return data;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}