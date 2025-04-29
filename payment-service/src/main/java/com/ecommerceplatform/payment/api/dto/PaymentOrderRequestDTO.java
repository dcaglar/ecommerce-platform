package com.ecommerceplatform.payment.api.dto;

public class PaymentOrderRequestDTO {
    private String sellerId;
    private AmountDto amount;

    // Default constructor
    public PaymentOrderRequestDTO() {}

    // Constructor for initialization
    public PaymentOrderRequestDTO(String sellerId, AmountDto amount) {
        this.sellerId = sellerId;
        this.amount = amount;
    }

    // Getters and Setters
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public AmountDto getAmount() {
        return amount;
    }

    public void setAmount(AmountDto amount) {
        this.amount = amount;
    }
}