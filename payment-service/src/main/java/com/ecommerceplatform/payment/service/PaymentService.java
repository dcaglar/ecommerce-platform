package com.ecommerceplatform.payment.service;

import com.ecommerceplatform.common.event.EventEnvelope;
import com.ecommerceplatform.common.event.OutboxEvent;
import com.ecommerceplatform.payment.api.dto.PaymentRequest;
import com.ecommerceplatform.payment.domain.model.Amount;
import com.ecommerceplatform.payment.domain.model.Payment;
import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import com.ecommerceplatform.payment.domain.event.PaymentOrderCreatedEvent;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.OutboxEventRepository;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.PaymentOrderRepository;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentOrderRepository paymentOrderRepository,
                          OutboxEventRepository outboxEventRepository,
                          ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentOrderRepository = paymentOrderRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void createPayment(PaymentRequest request) {
        String paymentId = UUID.randomUUID().toString();

        // Save Payment
        Payment payment = createPaymentEntity(paymentId, request);
        paymentRepository.save(payment);

        // Save Payment Orders
        List<PaymentOrder> paymentOrders = createPaymentOrders(paymentId, request.getPaymentOrders());
        paymentOrderRepository.saveAll(paymentOrders);

        // Create and Save Outbox Events for each PaymentOrder
        saveOutboxEvents(paymentOrders);
    }

    private Payment createPaymentEntity(String paymentId, PaymentRequest request) {
        return new Payment(
                paymentId,
                request.getBuyerId(),
                toDomainAmount(request.getTotalAmount()),
                request.getOrderId(),
                "INITIATED",
                LocalDateTime.now()
        );
    }

    private List<PaymentOrder> createPaymentOrders(String paymentId, List<com.ecommerceplatform.payment.api.dto.PaymentOrderRequest> paymentOrderRequests) {
        return paymentOrderRequests.stream()
                .map(poRequest -> new PaymentOrder(
                        UUID.randomUUID().toString(),
                        paymentId,
                        poRequest.getSellerId(),
                        toDomainAmount(poRequest.getAmount()),
                        "INITIATED",
                        0,
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());
    }

    private void saveOutboxEvents(List<PaymentOrder> paymentOrders) {
        List<OutboxEvent> outboxEvents = paymentOrders.stream()
                .map(paymentOrder -> {
                    PaymentOrderCreatedEvent event = new PaymentOrderCreatedEvent(
                            paymentOrder.getPaymentOrderId(),
                            paymentOrder.getPaymentId(),
                            paymentOrder.getSellerId(),
                            toAmountDto(paymentOrder.getAmount())
                    );
                    return createOutboxEvent(paymentOrder.getPaymentOrderId(), "payment_order_created", event);
                })
                .collect(Collectors.toList());

        outboxEventRepository.saveAll(outboxEvents);
    }

    private Amount toDomainAmount(com.ecommerceplatform.payment.api.dto.AmountDto dto) {
        return new Amount(dto.getValue(), Currency.getInstance(dto.getCurrency()));
    }

    private com.ecommerceplatform.payment.api.dto.AmountDto toAmountDto(Amount amount) {
        return new com.ecommerceplatform.payment.api.dto.AmountDto(
                amount.getValue(),
                amount.getCurrency().getCurrencyCode()
        );
    }

    private OutboxEvent createOutboxEvent(String aggregateId, String eventType, Object event) {
        try {
            EventEnvelope<Object> envelope = new EventEnvelope<>(
                    eventType,
                    aggregateId,
                    event,
                    Instant.now()
            );

            String payload = objectMapper.writeValueAsString(envelope);

            return new OutboxEvent(
                    null,             // ID is generated by DB
                    eventType,
                    aggregateId,
                    payload,
                    "NEW",
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create outbox event", e);
        }
    }
}