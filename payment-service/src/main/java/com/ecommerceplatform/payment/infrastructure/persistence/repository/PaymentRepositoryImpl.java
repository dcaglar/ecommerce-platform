package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.domain.model.Payment;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentEntity;
import com.ecommerceplatform.payment.infrastructure.persistence.mapper.PaymentMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpaRepository;

    public PaymentRepositoryImpl(JpaPaymentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = PaymentMapper.toEntity(payment);
        PaymentEntity savedEntity = jpaRepository.save(entity);
        return PaymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(String id) {
        return jpaRepository.findById(id)
                .map(PaymentMapper::toDomain);
    }
}