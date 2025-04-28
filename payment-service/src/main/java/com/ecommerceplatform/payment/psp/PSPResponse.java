package com.ecommerceplatform.payment.psp;

public class PSPResponse {
    private String status;

    public PSPResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}