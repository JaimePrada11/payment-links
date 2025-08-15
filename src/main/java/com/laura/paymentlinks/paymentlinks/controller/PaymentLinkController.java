package com.laura.paymentlinks.paymentlinks.controller;

import com.laura.paymentlinks.paymentlinks.dto.CreatePaymentLinkRequest;
import com.laura.paymentlinks.paymentlinks.dto.*;
import com.laura.paymentlinks.paymentlinks.dto.PaymentLinkResponse;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.util.Exceptions.ConflictException;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.service.PaymentLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Payment")
public class PaymentLinkController {

    private PaymentLinkService paymentLinkService;
    private  final HttpServletRequest http;

    @Autowired
    public PaymentLinkController(PaymentLinkService paymentLinkService, HttpServletRequest http) {
        this.paymentLinkService = paymentLinkService;
        this.http = http;
    }

    private Long merchantId() {
        Object m = http.getAttribute("merchantId");
        if (m == null) throw new ConflictException("Unauthorized");
        return (Long)m;
    }

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> create(@Valid @RequestBody CreatePaymentLinkRequest req) {

        var m = new Merchant();
        m.setId(merchantId());

        var resp = paymentLinkService.createPaymentLink(req, m);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);

    }

    @GetMapping("/{Ref}")
    public ResponseEntity<PaymentLinkResponse> getOne(@PathVariable String Ref) {
        PaymentLink link = paymentLinkService.find(merchantId(), Ref);
        return ResponseEntity.ok(paymentLinkService.map(link));
    }



}
