package com.laura.paymentlinks.paymentlinks.dto;


import com.fasterxml.jackson.databind.JsonNode;

public record CreatePaymentLinkRequest(
        Long amountCents,
        String currency,
        String description,
        Integer expiresInMinutes,
        JsonNode metadata
) {}
