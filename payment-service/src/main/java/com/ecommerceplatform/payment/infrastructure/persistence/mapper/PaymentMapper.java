package com.ecommerceplatform.payment.infrastructure.persistence.mapper;

import com.ecommerceplatform.payment.domain.model.Amount;
import com.ecommerceplatform.payment.domain.model.Payment;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentEntity;

import java.util.Currency;
import java.util.Objects;

public class PaymentMapper {

    private PaymentMapper() {
        // Private constructor to prevent instantiation
    }

    public static PaymentEntity toEntity(Payment payment) {
        Objects.requireNonNull(payment, "payment must not be null");

        return new PaymentEntity(
                payment.getPaymentId(),
                payment.getBuyerId(),
                payment.getTotalAmount().getValue(),
                payment.getTotalAmount().getCurrency().getCurrencyCode(),
                payment.getOrderId(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }

    public static Payment toDomain(PaymentEntity entity) {
        Objects.requireNonNull(entity, "paymentEntity must not be null");

        Amount amount = new Amount(entity.getTotalAmountValue(), Currency.getInstance(entity.getTotalAmountCurrency()));

        return new Payment(
                entity.getPaymentId(),
                entity.getBuyerId(),
                amount,
                entity.getOrderId(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}