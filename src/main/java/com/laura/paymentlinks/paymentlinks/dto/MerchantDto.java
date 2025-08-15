package com.laura.paymentlinks.paymentlinks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MerchantDto(

        @NotNull
        String name,

        @Email
        @NotNull
        String email,

        List<PaymentLinkDto> paymentLinks
) {}
