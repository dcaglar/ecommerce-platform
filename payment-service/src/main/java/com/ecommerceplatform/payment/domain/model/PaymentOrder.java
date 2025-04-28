package com.ecommerceplatform.payment.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentOrder {

    private final String paymentOrderId;
    private final String paymentId;
    private final String sellerId;
    private final Amount amount;
    private final String status;  // INITIATED, SUCCESS, FAILED
    private final int retryCount;
    private final LocalDateTime createdAt;

    public PaymentOrder(String paymentOrderId, String paymentId, String sellerId, Amount amount,
                        String status, int retryCount, LocalDateTime createdAt) {
        this.paymentOrderId = Objects.requireNonNull(paymentOrderId);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.sellerId = Objects.requireNonNull(sellerId);
        this.amount = Objects.requireNonNull(amount);
        this.status = Objects.requireNonNull(status);
        this.retryCount = retryCount;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    // Getters

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Domain methods

    public PaymentOrder markAsSuccess() {
        return new PaymentOrder(
                this.paymentOrderId,
                this.paymentId,
                this.sellerId,
                this.amount,
                "SUCCESS",
                this.retryCount,
                this.createdAt
        );
    }

    public PaymentOrder incrementRetry() {
        return new PaymentOrder(
                this.paymentOrderId,
                this.paymentId,
                this.sellerId,
                this.amount,
                this.status,
                this.retryCount + 1,
                this.createdAt
        );
    }

    public PaymentOrder markAsFailed() {
        return new PaymentOrder(
                this.paymentOrderId,
                this.paymentId,
                this.sellerId,
                this.amount,
                "FAILED",
                this.retryCount,
                this.createdAt
        );
    }
}