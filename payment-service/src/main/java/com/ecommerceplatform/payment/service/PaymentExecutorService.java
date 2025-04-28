package com.ecommerceplatform.payment.service;

import com.ecommerceplatform.common.event.EventEnvelope;
import com.ecommerceplatform.payment.domain.event.PaymentOrderCreatedEvent;
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
public class PaymentExecutorService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final PSPClient pspClient;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ObjectMapper objectMapper;

    public PaymentExecutorService(PaymentOrderRepository paymentOrderRepository,
                                  PSPClient pspClient,
                                  PaymentEventPublisher paymentEventPublisher,
                                  ObjectMapper objectMapper) {
        this.paymentOrderRepository = paymentOrderRepository;
        this.pspClient = pspClient;
        this.paymentEventPublisher = paymentEventPublisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment_order_created", groupId = "payment-executor-group")
    @Transactional
    public void processInitialPayment(ConsumerRecord<String, String> record) {
        try {
            String payload = record.value();
            EventEnvelope<?> envelope = objectMapper.readValue(payload, EventEnvelope.class);

            PaymentOrderCreatedEvent event = objectMapper.convertValue(envelope.getData(), PaymentOrderCreatedEvent.class);

            // 1. Load Entity
            PaymentOrder paymentOrder = paymentOrderRepository.findById(event.getPaymentOrderId())
                    .orElseThrow(() -> new RuntimeException("PaymentOrder not found: " + event.getPaymentOrderId()));

            // 2. Map Entity → Domain
            if (!"INITIATED".equals(paymentOrder.getStatus())) {
                return; // Already processed
            }

            PSPResponse response = safePspCall(paymentOrder);

            if ("SUCCESS".equals(response.getStatus())) {
                // 3. Update domain object
                PaymentOrder updatedPaymentOrder = paymentOrder.markAsSuccess();

                // 4. Map back Domain → Entity and save
                paymentOrderRepository.save(updatedPaymentOrder);

                paymentEventPublisher.publish(
                        "payment_order_success",
                        updatedPaymentOrder.getPaymentOrderId(),
                        "payment_order_success",
                        new PaymentOrderSucceededEvent(
                                updatedPaymentOrder.getPaymentOrderId(),
                                updatedPaymentOrder.getSellerId(),
                                updatedPaymentOrder.getAmount().getValue(),
                                updatedPaymentOrder.getAmount().getCurrency().getCurrencyCode()
                        )
                );

            } else {
                // PSP failed → publish PaymentOrderRetryEvent
                PaymentOrderRetryEvent retryEvent = new PaymentOrderRetryEvent(
                        paymentOrder.getPaymentOrderId(),
                        paymentOrder.getPaymentId(),
                        paymentOrder.getSellerId(),
                        paymentOrder.getAmount().getValue(),
                        paymentOrder.getAmount().getCurrency().getCurrencyCode(),
                        1 // first retry
                );

                paymentEventPublisher.publish(
                        "payment_order_retry",
                        paymentOrder.getPaymentOrderId(),
                        "payment_order_retry",
                        retryEvent
                );
            }

        } catch (Exception e) {
            System.err.println("Failed to process initial payment: " + e.getMessage());
        }
    }

    private PSPResponse safePspCall(PaymentOrder paymentOrder) throws Exception {
        CompletableFuture<PSPResponse> future = CompletableFuture.supplyAsync(() -> pspClient.charge(paymentOrder));
        return future.get(3, TimeUnit.SECONDS);
    }
}