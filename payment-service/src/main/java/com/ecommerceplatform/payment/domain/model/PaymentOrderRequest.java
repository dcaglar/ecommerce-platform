package com.ecommerceplatform.payment.domain.model;

import com.ecommerceplatform.payment.api.dto.AmountDto;

import java.util.Objects;

public class PaymentOrderRequest {

    private final String sellerId;
    private final AmountDto amount;

    public PaymentOrderRequest(String sellerId, AmountDto amount) {
        this.sellerId = Objects.requireNonNull(sellerId, "sellerId must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
    }

    public String getSellerId() {
        return sellerId;
    }

    public AmountDto getAmount() {
        return amount;
    }
}
    // Getters}