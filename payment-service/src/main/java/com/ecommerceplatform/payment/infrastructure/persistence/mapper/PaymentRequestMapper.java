package com.ecommerceplatform.payment.infrastructure.persistence.mapper;


import com.ecommerceplatform.payment.api.dto.AmountDto;
import com.ecommerceplatform.payment.api.dto.PaymentOrderRequestDTO;
import com.ecommerceplatform.payment.api.dto.PaymentRequestDTO;
import com.ecommerceplatform.payment.domain.model.PaymentOrderRequest;
import com.ecommerceplatform.payment.domain.model.PaymentRequest;

import java.util.stream.Collectors;

public class PaymentRequestMapper {

    public static PaymentRequest toDomain(PaymentRequestDTO dto) {
        return new PaymentRequest(
                dto.getBuyerId(),
                new AmountDto(dto.getTotalAmount().getValue(),dto.getTotalAmount().getCurrency()),
                dto.getOrderId(),
                dto.getPaymentOrders().stream()
                        .map(po -> new PaymentOrderRequest(
                                po.getSellerId(),
                                po.getAmount()))
                        .collect(Collectors.toList())
        );
    }

    public static PaymentRequestDTO toDto(PaymentRequest domain) {
        return new PaymentRequestDTO(
                domain.getBuyerId(),
                new AmountDto(domain.getTotalAmount().getValue(), domain.getTotalAmount().getCurrency().getCurrencyCode()),
                domain.getOrderId(),
                domain.getPaymentOrders().stream()
                        .map(po -> new PaymentOrderRequestDTO(
                                po.getSellerId(),
                                new AmountDto(po.getAmount().getValue(), po.getAmount().getCurrency())))
                        .collect(Collectors.toList())
        );
    }
}