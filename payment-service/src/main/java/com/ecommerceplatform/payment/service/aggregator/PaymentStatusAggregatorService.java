package com.ecommerceplatform.payment.service.aggregator;

import com.ecommerceplatform.common.event.EventEnvelope;
import com.ecommerceplatform.payment.domain.event.PaymentOrderFailedEvent;
import com.ecommerceplatform.payment.domain.event.PaymentOrderSucceededEvent;
import com.ecommerceplatform.payment.domain.model.Payment;
import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import com.ecommerceplatform.payment.infrastructure.kafka.PaymentEventPublisher;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.PaymentOrderRepository;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

@Service
public class PaymentStatusAggregatorService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ObjectMapper objectMapper;

    public PaymentStatusAggregatorService(PaymentOrderRepository paymentOrderRepository,
                                          PaymentRepository paymentRepository,
                                          PaymentEventPublisher paymentEventPublisher,
                                          ObjectMapper objectMapper) {
        this.paymentOrderRepository = paymentOrderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentEventPublisher = paymentEventPublisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"payment_order_success", "payment_order_failed"}, groupId = "payment-aggregator-group")
    @Transactional
    public void aggregatePaymentStatus(ConsumerRecord<String, String> record) {
        try {
            String payload = record.value();
            EventEnvelope<?> envelope = objectMapper.readValue(payload, EventEnvelope.class);

            String paymentOrderId = envelope.getAggregateId();  // PaymentOrderId

            String paymentId;

            if ("payment_order_success".equals(record.topic())) {
                PaymentOrderSucceededEvent event = objectMapper.convertValue(envelope.getData(), PaymentOrderSucceededEvent.class);
                paymentId = findPaymentIdByPaymentOrderId(event.getPaymentOrderId());
            } else if ("payment_order_failed".equals(record.topic())) {
                PaymentOrderFailedEvent event = objectMapper.convertValue(envelope.getData(), PaymentOrderFailedEvent.class);
                paymentId = findPaymentIdByPaymentOrderId(event.getPaymentOrderId());
            } else {
                throw new RuntimeException("Unknown topic: " + record.topic());
            }

            if (paymentId == null) {
                System.err.println("Payment ID not found for PaymentOrder: " + paymentOrderId);
                return;
            }

            if (allPaymentOrdersProcessed(paymentId)) {
                finalizePayment(paymentId);
            }

        } catch (Exception e) {
            System.err.println("Failed to aggregate payment status: " + e.getMessage());
        }
    }

    private String findPaymentIdByPaymentOrderId(String paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId)
                .map(PaymentOrder::getPaymentId)
                .orElse(null);
    }

    private boolean allPaymentOrdersProcessed(String paymentId) {
        long total = paymentOrderRepository.countByPaymentId(paymentId);
        long completed = paymentOrderRepository.countByPaymentIdAndStatusIn(paymentId, Arrays.asList("SUCCESS", "FAILED"));

        return total > 0 && total == completed;
    }

    private void finalizePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        boolean hasFailure = paymentOrderRepository.existsByPaymentIdAndStatus(paymentId, "FAILED");

        if (hasFailure) {
            Payment failedPayment = payment.markAsFailed();
            paymentRepository.save(failedPayment);

            paymentEventPublisher.publish(
                    "payment_failed",
                    failedPayment.getPaymentId(),
                    "payment_failed",
                    null // You can create a PaymentFailedEvent if you want
            );
        } else {
            Payment successPayment = payment.markAsSuccess();
            paymentRepository.save(successPayment);

            paymentEventPublisher.publish(
                    "payment_success",
                    successPayment.getPaymentId(),
                    "payment_success",
                    null // You can create a PaymentSuccessEvent if you want
            );
        }
    }
}