package com.ecommerceplatform.payment.controller;

import com.ecommerceplatform.payment.api.dto.PaymentRequestDTO;
import com.ecommerceplatform.payment.domain.model.PaymentRequest;
import com.ecommerceplatform.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Endpoint to create a payment.
     * Requires the user to have the 'payment:write' role to access this endpoint.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('payment:write')")
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        paymentService.createPayment(paymentRequest);
        return ResponseEntity.ok("Payment successfully created.");
    }
}