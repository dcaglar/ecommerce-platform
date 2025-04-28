package com.ecommerceplatform.payment.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Amount {

    private final BigDecimal value;
    private final Currency currency;

    public Amount(BigDecimal value, Currency currency) {
        if (value == null || currency == null) {
            throw new IllegalArgumentException("Value and Currency must not be null");
        }
        if (value.scale() > 2) {
            throw new IllegalArgumentException("Amount value cannot have more than two decimal places");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount value cannot be negative");
        }
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Amount add(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Amounts with different currencies");
        }
        return new Amount(this.value.add(other.value), this.currency);
    }

    public Amount subtract(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract Amounts with different currencies");
        }
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Resulting Amount cannot be negative");
        }
        return new Amount(result, this.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amount)) return false;
        Amount amount = (Amount) o;
        return value.equals(amount.value) && currency.equals(amount.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }

    @Override
    public String toString() {
        return value + " " + currency.getCurrencyCode();
    }
}