package com.ecommerceplatform.payment.domain.event;

public class PaymentOrderCancelledEvent {
    private final String paymentOrderId;
    private final String reason;

    public PaymentOrderCancelledEvent(String paymentOrderId, String reason) {
        this.paymentOrderId = paymentOrderId;
        this.reason = reason;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getReason() {
        return reason;
    }
}