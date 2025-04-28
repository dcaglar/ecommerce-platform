package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentOrderRepository extends JpaRepository<PaymentOrderEntity, String> {
    // JpaRepository gives you basic CRUD: findById, save, delete, etc
}