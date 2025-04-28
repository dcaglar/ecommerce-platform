package com.ecommerceplatform.payment.domain.event;

public class PaymentOrderCompletedEvent {
    private final String paymentOrderId;
    private final String paymentId;

    public PaymentOrderCompletedEvent(String paymentOrderId, String paymentId) {
        this.paymentOrderId = paymentOrderId;
        this.paymentId = paymentId;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getPaymentId() {
        return paymentId;
    }
}