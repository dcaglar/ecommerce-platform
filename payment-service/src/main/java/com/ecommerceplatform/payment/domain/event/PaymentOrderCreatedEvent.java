package com.ecommerceplatform.payment.domain.event;

import com.ecommerceplatform.payment.api.dto.AmountDto;
import java.util.Objects;

public class PaymentOrderCreatedEvent {

    private final String paymentOrderId;
    private final String paymentId;
    private final String sellerId;
    private final AmountDto amount;

    public PaymentOrderCreatedEvent(String paymentOrderId, String paymentId, String sellerId, AmountDto amount) {
        this.paymentOrderId = Objects.requireNonNull(paymentOrderId);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.sellerId = Objects.requireNonNull(sellerId);
        this.amount = Objects.requireNonNull(amount);
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

    public AmountDto getAmount() {
        return amount;
    }
}