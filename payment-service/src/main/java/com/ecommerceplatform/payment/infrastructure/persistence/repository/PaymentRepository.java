package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.domain.model.Payment;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(String id);
}