package com.laura.paymentlinks.paymentlinks.controller;

import com.laura.paymentlinks.paymentlinks.dto.PaymentRequest;
import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.service.PaymentLinkService;
import com.laura.paymentlinks.paymentlinks.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-links")
public class PaymentController {
    private final PaymentService payments;
    private final PaymentLinkService links;
    private final HttpServletRequest http;

    public PaymentController(PaymentService payments, PaymentLinkService links, HttpServletRequest http) {
        this.payments = payments; this.links = links; this.http = http;
    }

    private Long merchantId() { return (Long) http.getAttribute("merchantId"); }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable Long id, @Valid @RequestBody PaymentRequest body, @RequestHeader("Idempotency-Key") String idemKey) {
        PaymentAttempt attempt = payments.pay(id, merchantId(), idemKey, body);
        if (attempt.getStatus()== PaymentAttempt.AttemptStatus.SUCCESS) {
            PaymentLink link = links.find(merchantId(), String.valueOf(id));
            return ResponseEntity.ok(links.map(link));
        } else {
            return ResponseEntity.status(422).body(java.util.Map.of("status","FAILED","reason", attempt.getReason()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        var link = payments.cancel(id, merchantId());
        return ResponseEntity.ok(links.map(link));
    }
}