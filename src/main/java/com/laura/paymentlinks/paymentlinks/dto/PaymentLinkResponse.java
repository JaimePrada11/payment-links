package com.laura.paymentlinks.paymentlinks.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record PaymentLinkResponse(
        String id,
        String reference,
        String status,
        LocalDateTime expires_at,
        Long amount_cents,
        String currency,
        String description,
        LocalDateTime paid_at,
        JsonNode metadata,
        LocalDateTime created_at
) {}