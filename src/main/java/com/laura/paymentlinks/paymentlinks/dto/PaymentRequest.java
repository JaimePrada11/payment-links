package com.laura.paymentlinks.paymentlinks.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(@NotBlank String payment_token) {}

