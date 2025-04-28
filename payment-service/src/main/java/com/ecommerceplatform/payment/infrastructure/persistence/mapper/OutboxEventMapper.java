package com.ecommerceplatform.payment.infrastructure.persistence.mapper;

import com.ecommerceplatform.common.event.OutboxEvent;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.OutboxEventEntity;

public class OutboxEventMapper {

    private OutboxEventMapper() {
        // prevent instantiation
    }

    public static OutboxEventEntity toEntity(OutboxEvent domain) {
        return new OutboxEventEntity(
                domain.getEventType(),
                domain.getAggregateId(),
                domain.getPayload(),
                domain.getStatus(),
                domain.getCreatedAt()
        );
    }

    public static OutboxEvent toDomain(OutboxEventEntity entity) {
        return new OutboxEvent(
                entity.getId(),
                entity.getEventType(),
                entity.getAggregateId(),
                entity.getPayload(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}