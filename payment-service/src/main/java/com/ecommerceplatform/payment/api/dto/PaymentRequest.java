package com.ecommerceplatform.payment.api.dto;

import java.util.List;
import java.util.Objects;

public class PaymentRequest {

    private final String buyerId;
    private final AmountDto totalAmount;
    private final String orderId;
    private final List<PaymentOrderRequest> paymentOrders;

    public PaymentRequest(String buyerId, AmountDto totalAmount, String orderId, List<PaymentOrderRequest> paymentOrders) {
        this.buyerId = Objects.requireNonNull(buyerId, "buyerId must not be null");
        this.totalAmount = Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
        this.paymentOrders = Objects.requireNonNull(paymentOrders, "paymentOrders must not be null");
    }

    public String getBuyerId() {
        return buyerId;
    }

    public AmountDto getTotalAmount() {
        return totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<PaymentOrderRequest> getPaymentOrders() {
        return paymentOrders;
    }
}