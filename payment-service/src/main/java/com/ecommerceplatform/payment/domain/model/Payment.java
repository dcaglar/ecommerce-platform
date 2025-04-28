package com.ecommerceplatform.payment.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {
    private final String paymentId;
    private final String buyerId;
    private final Amount totalAmount;
    private final String orderId;   // Just a String reference
    private String status;
    private final LocalDateTime createdAt;

    public Payment(String paymentId, String buyerId, Amount totalAmount, String orderId, String status, LocalDateTime createdAt) {
        this.paymentId = Objects.requireNonNull(paymentId);
        this.buyerId = Objects.requireNonNull(buyerId);
        this.totalAmount = Objects.requireNonNull(totalAmount);
        this.orderId = Objects.requireNonNull(orderId);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    // Getters and setters (or other methods you need)
    public String getPaymentId() {
        return paymentId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Mark as Failed
    public Payment markAsFailed() {
        this.status = "FAILED"; // Update status to "FAILED"
        return this;
    }

    // Mark as Success
    public Payment markAsSuccess() {
        this.status = "SUCCESS"; // Update status to "SUCCESS"
        return this;
    }
}