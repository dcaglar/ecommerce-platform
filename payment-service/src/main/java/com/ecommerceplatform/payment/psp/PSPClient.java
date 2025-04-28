package com.ecommerceplatform.payment.psp;

import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PSPClient {

    private final Random random = new Random();

    public PSPResponse charge(PaymentOrder paymentOrder) {
        // Simulate network latency
        simulateDelay();

        // Randomly simulate success or failure
        boolean success = random.nextDouble() > 0.2; // 80% success rate

        if (success) {
            return new PSPResponse("SUCCESS");
        } else {
            return new PSPResponse("FAILED");
        }
    }

    private void simulateDelay() {
        try {
            Thread.sleep(300 + random.nextInt(700)); // Random delay between 300ms to 1s
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}