package com.ecommerceplatform.payment.api.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class AmountDto {
    private  BigDecimal value;
    private  String currency; // example "EUR", "USD"

    public AmountDto(){

    }
    public AmountDto(BigDecimal value, String currency) {
        this.value = Objects.requireNonNull(value);
        this.currency = Objects.requireNonNull(currency);
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}