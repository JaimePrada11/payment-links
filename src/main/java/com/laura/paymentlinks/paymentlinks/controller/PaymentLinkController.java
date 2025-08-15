package com.laura.paymentlinks.paymentlinks.controller;

import com.laura.paymentlinks.paymentlinks.dto.PaymentLinkDto;
import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.service.PaymentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Payment")
public class PaymentLinkController {
    @Autowired
    private PaymentLinkService paymentLinkService;

    @PostMapping()
    public ResponseEntity<PaymentLink> save(@RequestBody PaymentLinkDto dto, @RequestParam Long idMerchant) {
        PaymentLink response = paymentLinkService.createPaymentLink(dto, idMerchant);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("{id}/pay")
    public ResponseEntity<PaymentLink> pay(@PathVariable("id") Long paymentLinkId,
                                           @RequestParam String idempotencyKey,
                                           @RequestParam String token){
        PaymentLink pay = paymentLinkService.processPayment(paymentLinkId, idempotencyKey, token);
        return   ResponseEntity.ok(pay);
    }
}
