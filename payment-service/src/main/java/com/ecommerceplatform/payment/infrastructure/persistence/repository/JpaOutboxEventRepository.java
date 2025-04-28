package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.infrastructure.persistence.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOutboxEventRepository extends JpaRepository<OutboxEventEntity, Long> {
    // Basic save, findById already available
}