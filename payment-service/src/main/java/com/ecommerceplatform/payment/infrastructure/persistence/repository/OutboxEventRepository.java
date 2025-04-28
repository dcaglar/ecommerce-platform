package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.common.event.OutboxEvent;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository {
    OutboxEvent save(OutboxEvent event);
    Optional<OutboxEvent> findById(Long id);
    void saveAll(List<OutboxEvent> events); // ✅ Batch method

}