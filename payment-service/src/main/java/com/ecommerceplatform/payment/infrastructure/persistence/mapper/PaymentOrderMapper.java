package com.ecommerceplatform.payment.infrastructure.persistence.mapper;

import com.ecommerceplatform.payment.domain.model.Amount;
import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentOrderEntity;

import java.util.Currency;
import java.util.Objects;

public final class PaymentOrderMapper {

    private PaymentOrderMapper() {
        // Utility class, no instantiation
    }

    public static PaymentOrderEntity toEntity(PaymentOrder paymentOrder) {
        Objects.requireNonNull(paymentOrder, "paymentOrder must not be null");

        return new PaymentOrderEntity(
                paymentOrder.getPaymentOrderId(),
                paymentOrder.getPaymentId(),
                paymentOrder.getSellerId(),
                paymentOrder.getAmount().getValue(),
                paymentOrder.getAmount().getCurrency().getCurrencyCode(),
                paymentOrder.getStatus(),
                paymentOrder.getRetryCount(),
                paymentOrder.getCreatedAt()
        );
    }

    public static PaymentOrder toDomain(PaymentOrderEntity entity) {
        Objects.requireNonNull(entity, "paymentOrderEntity must not be null");

        Amount amount = new Amount(entity.getAmountValue(), Currency.getInstance(entity.getAmountCurrency()));

        return new PaymentOrder(
                entity.getPaymentOrderId(),
                entity.getPaymentId(),
                entity.getSellerId(),
                amount,
                entity.getStatus(),
                entity.getRetryCount(),
                entity.getCreatedAt()
        );
    }
}