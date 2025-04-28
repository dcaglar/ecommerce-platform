package com.ecommerceplatform.payment.domain.event;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentOrderRetryEvent {

    private final String paymentOrderId;
    private final String paymentId;
    private final String sellerId;
    private final BigDecimal amountValue;
    private final String amountCurrency;
    private final int retryCount;

    public PaymentOrderRetryEvent(String paymentOrderId, String paymentId, String sellerId,
                                  BigDecimal amountValue, String amountCurrency, int retryCount) {
        this.paymentOrderId = Objects.requireNonNull(paymentOrderId);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.sellerId = Objects.requireNonNull(sellerId);
        this.amountValue = Objects.requireNonNull(amountValue);
        this.amountCurrency = Objects.requireNonNull(amountCurrency);
        this.retryCount = retryCount;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getPaymentId() {
        return paymentId;
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

    public int getRetryCount() {
        return retryCount;
    }
}