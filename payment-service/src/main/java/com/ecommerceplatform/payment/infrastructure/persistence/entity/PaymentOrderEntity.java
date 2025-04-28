package com.ecommerceplatform.payment.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_orders")
public class PaymentOrderEntity {

    @Id
    private String paymentOrderId;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private BigDecimal amountValue;  // 💵 Split value
    @Column(nullable = false)
    private String amountCurrency;   // 💵 Split currency

    @Column(nullable = false)
    private String status; // INITIATED, SUCCESS, FAILED

    @Column(nullable = false)
    private int retryCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PaymentOrderEntity() {
        // for JPA
    }

    public PaymentOrderEntity(String paymentOrderId, String paymentId, String sellerId,
                               BigDecimal amountValue, String amountCurrency,
                               String status, int retryCount, LocalDateTime createdAt) {
        this.paymentOrderId = paymentOrderId;
        this.paymentId = paymentId;
        this.sellerId = sellerId;
        this.amountValue = amountValue;
        this.amountCurrency = amountCurrency;
        this.status = status;
        this.retryCount = retryCount;
        this.createdAt = createdAt;
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

    public String getStatus() {
        return status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Getters and Setters
}