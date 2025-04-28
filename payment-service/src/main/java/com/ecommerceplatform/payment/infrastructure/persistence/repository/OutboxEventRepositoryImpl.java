package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.common.event.OutboxEvent;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.OutboxEventEntity;
import com.ecommerceplatform.payment.infrastructure.persistence.mapper.OutboxEventMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OutboxEventRepositoryImpl implements OutboxEventRepository {

    private final JpaOutboxEventRepository jpaRepository;

    public OutboxEventRepositoryImpl(JpaOutboxEventRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public OutboxEvent save(OutboxEvent event) {
        OutboxEventEntity entity = OutboxEventMapper.toEntity(event);
        OutboxEventEntity saved = jpaRepository.save(entity);
        return OutboxEventMapper.toDomain(saved);
    }

    @Override
    public void saveAll(List<OutboxEvent> events) {
        List<OutboxEventEntity> entities = events.stream()
                .map(OutboxEventMapper::toEntity)
                .collect(Collectors.toList());

        jpaRepository.saveAll(entities);
    }

    @Override
    public Optional<OutboxEvent> findById(Long id) {
        return jpaRepository.findById(id)
                .map(OutboxEventMapper::toDomain);
    }
}