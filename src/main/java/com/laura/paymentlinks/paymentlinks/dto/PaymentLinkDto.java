package com.laura.paymentlinks.paymentlinks.dto;

import com.laura.paymentlinks.paymentlinks.model.PaymentLink;

import java.time.LocalDateTime;

public record PaymentLinkDto(
        String reference,
        Long amountCents,
        String currency,
        String description,
        PaymentLink.PaymentStatus status,
        LocalDateTime expiresAt,
        LocalDateTime paidAt,
        String metadata,
        LocalDateTime createdAt
) {}
