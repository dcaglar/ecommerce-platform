package com.ecommerceplatform.payment.api.dto;

import java.util.List;
import java.util.Objects;

public class PaymentRequestDTO {
    private String buyerId;
    private AmountDto totalAmount;
    private String orderId;
    private List<PaymentOrderRequestDTO> paymentOrders;

    // Default constructor
    public PaymentRequestDTO() {}

    // Constructor for initialization
    public PaymentRequestDTO(String buyerId, AmountDto totalAmount, String orderId, List<PaymentOrderRequestDTO> paymentOrders) {
        this.buyerId = Objects.requireNonNull(buyerId);
        this.totalAmount = Objects.requireNonNull(totalAmount);
        this.orderId = Objects.requireNonNull(orderId);
        this.paymentOrders = Objects.requireNonNull(paymentOrders);
    }

    // Getters and Setters
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public AmountDto getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(AmountDto totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PaymentOrderRequestDTO> getPaymentOrders() {
        return paymentOrders;
    }

    public void setPaymentOrders(List<PaymentOrderRequestDTO> paymentOrders) {
        this.paymentOrders = paymentOrders;
    }
}