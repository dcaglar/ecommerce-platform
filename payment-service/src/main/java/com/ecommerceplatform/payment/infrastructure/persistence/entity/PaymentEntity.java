package com.ecommerceplatform.payment.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private String paymentId;

    @Column(nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private BigDecimal totalAmountValue;  // 💵 Amount value

    @Column(nullable = false)
    private String totalAmountCurrency;   // 💵 Currency code (e.g., "USD")

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String status; // INITIATED, SUCCESS, FAILED, etc.

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PaymentEntity() {
        // JPA requires a no-arg constructor
    }

    public PaymentEntity(String paymentId, String buyerId, BigDecimal totalAmountValue, String totalAmountCurrency,
                         String orderId, String status, LocalDateTime createdAt) {
        this.paymentId = Objects.requireNonNull(paymentId, "paymentId must not be null");
        this.buyerId = Objects.requireNonNull(buyerId, "buyerId must not be null");
        this.totalAmountValue = Objects.requireNonNull(totalAmountValue, "totalAmountValue must not be null");
        this.totalAmountCurrency = Objects.requireNonNull(totalAmountCurrency, "totalAmountCurrency must not be null");
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    // Getters

    public String getPaymentId() {
        return paymentId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public BigDecimal getTotalAmountValue() {
        return totalAmountValue;
    }

    public String getTotalAmountCurrency() {
        return totalAmountCurrency;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters

    public void setPaymentId(String paymentId) {
        this.paymentId = Objects.requireNonNull(paymentId, "paymentId must not be null");
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = Objects.requireNonNull(buyerId, "buyerId must not be null");
    }

    public void setTotalAmountValue(BigDecimal totalAmountValue) {
        this.totalAmountValue = Objects.requireNonNull(totalAmountValue, "totalAmountValue must not be null");
    }

    public void setTotalAmountCurrency(String totalAmountCurrency) {
        this.totalAmountCurrency = Objects.requireNonNull(totalAmountCurrency, "totalAmountCurrency must not be null");
    }

    public void setOrderId(String orderId) {
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
    }

    public void setStatus(String status) {
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }
}