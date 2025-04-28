package com.ecommerceplatform.payment.service;

import com.ecommerceplatform.common.event.EventEnvelope;
import com.ecommerceplatform.payment.domain.event.PaymentOrderFailedEvent;
import com.ecommerceplatform.payment.domain.event.PaymentOrderRetryEvent;
import com.ecommerceplatform.payment.domain.event.PaymentOrderSucceededEvent;
import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import com.ecommerceplatform.payment.infrastructure.kafka.PaymentEventPublisher;
import com.ecommerceplatform.payment.infrastructure.persistence.repository.PaymentOrderRepository;
import com.ecommerceplatform.payment.psp.PSPClient;
import com.ecommerceplatform.payment.psp.PSPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentRetryExecutorService {

    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_BACKOFF = 5;  // Initial retry delay in seconds
    private static final int MAX_BACKOFF = 60; // Max retry delay in seconds

    private final PaymentOrderRepository paymentOrderRepository;
    private final PSPClient pspClient;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ObjectMapper objectMapper;

    public PaymentRetryExecutorService(PaymentOrderRepository paymentOrderRepository,
                                       PSPClient pspClient,
                                       PaymentEventPublisher paymentEventPublisher,
                                       ObjectMapper objectMapper) {
        this.paymentOrderRepository = paymentOrderRepository;
        this.pspClient = pspClient;
        this.paymentEventPublisher = paymentEventPublisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment_order_retry", groupId = "payment-retry-executor-group")
    @Transactional
    public void processRetry(ConsumerRecord<String, String> record) {
        try {
            String payload = record.value();
            EventEnvelope<?> envelope = objectMapper.readValue(payload, EventEnvelope.class);
            PaymentOrderRetryEvent event = objectMapper.convertValue(envelope.getData(), PaymentOrderRetryEvent.class);

            PaymentOrder paymentOrder = paymentOrderRepository.findById(event.getPaymentOrderId())
                    .orElseThrow(() -> new RuntimeException("PaymentOrder not found: " + event.getPaymentOrderId()));

            if ("SUCCESS".equals(paymentOrder.getStatus()) || "FAILED".equals(paymentOrder.getStatus())) {
                return; // already processed
            }

            PSPResponse response = safePspCall(paymentOrder);

            if ("SUCCESS".equals(response.getStatus())) {
                PaymentOrder successOrder = paymentOrder.markAsSuccess();
                paymentOrderRepository.save(successOrder);

                paymentEventPublisher.publish(
                        "payment_order_success",
                        successOrder.getPaymentOrderId(),
                        "payment_order_success",
                        new PaymentOrderSucceededEvent(
                                successOrder.getPaymentOrderId(),
                                successOrder.getSellerId(),
                                successOrder.getAmount().getValue(),
                                successOrder.getAmount().getCurrency().getCurrencyCode()
                        )
                );
            } else {
                int nextRetry = event.getRetryCount() + 1;

                if (nextRetry > MAX_RETRIES) {
                    // Push to DLQ after max retries
                    PaymentOrder failedOrder = paymentOrder.markAsFailed();
                    paymentOrderRepository.save(failedOrder);

                    paymentEventPublisher.publish(
                            "payment_order_failed_dlq",  // Publish to the Dead Letter Queue topic
                            failedOrder.getPaymentOrderId(),
                            "payment_order_failed",
                            new PaymentOrderFailedEvent(
                                    failedOrder.getPaymentOrderId(),
                                    failedOrder.getSellerId(),
                                    failedOrder.getAmount().getValue(),
                                    failedOrder.getAmount().getCurrency().getCurrencyCode()
                            )
                    );
                } else {
                    // Implement exponential backoff
                    int backoffTime = Math.min(INITIAL_BACKOFF * (int) Math.pow(2, nextRetry - 1), MAX_BACKOFF);
                    // Wait for backoff time before retrying
                    TimeUnit.SECONDS.sleep(backoffTime);

                    PaymentOrderRetryEvent retryEvent = new PaymentOrderRetryEvent(
                            paymentOrder.getPaymentOrderId(),
                            paymentOrder.getPaymentId(),
                            paymentOrder.getSellerId(),
                            paymentOrder.getAmount().getValue(),
                            paymentOrder.getAmount().getCurrency().getCurrencyCode(),
                            nextRetry
                    );

                    paymentEventPublisher.publish(
                            "payment_order_retry",
                            paymentOrder.getPaymentOrderId(),
                            "payment_order_retry",
                            retryEvent
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to process retry payment order: " + e.getMessage());
        }
    }

    private PSPResponse safePspCall(PaymentOrder paymentOrder) throws Exception {
        CompletableFuture<PSPResponse> future = CompletableFuture.supplyAsync(() -> pspClient.charge(paymentOrder));
        return future.get(3, TimeUnit.SECONDS);  // PSP call timeout (3 seconds)
    }
}