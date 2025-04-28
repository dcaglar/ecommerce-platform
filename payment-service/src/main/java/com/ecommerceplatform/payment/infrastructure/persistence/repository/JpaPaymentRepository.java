package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, String> {
    // We don't need to define any custom methods because JpaRepository provides them
}