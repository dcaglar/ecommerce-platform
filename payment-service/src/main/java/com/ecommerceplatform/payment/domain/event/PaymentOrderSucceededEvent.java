package com.ecommerceplatform.payment.domain.event;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentOrderSucceededEvent {

    private final String paymentOrderId;
    private final String sellerId;
    private final BigDecimal amountValue;
    private final String amountCurrency;

    public PaymentOrderSucceededEvent(String paymentOrderId, String sellerId, BigDecimal amountValue, String amountCurrency) {
        this.paymentOrderId = Objects.requireNonNull(paymentOrderId);
        this.sellerId = Objects.requireNonNull(sellerId);
        this.amountValue = Objects.requireNonNull(amountValue);
        this.amountCurrency = Objects.requireNonNull(amountCurrency);
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public BigDecimal getAmountValue() {
        return amountValue;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }
}