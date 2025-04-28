package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.domain.model.Payment;
import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentOrderRepository {
    // Custom methods, no need for flush(), save(), etc. - Spring Data JPA handles it
    long countByPaymentId(String paymentId);

    long countByPaymentIdAndStatusIn(String paymentId, List<String> statuses);

    boolean existsByPaymentIdAndStatus(String paymentId, String status);

    void saveAll(List<PaymentOrder> paymentOrders);

    void save(PaymentOrder paymentOrder);


    Optional<PaymentOrder> findById(String id);


}