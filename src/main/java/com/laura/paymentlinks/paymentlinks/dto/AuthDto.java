package com.laura.paymentlinks.paymentlinks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AuthDto(
        @Email
        @NotNull
        String email,

        String password
) {
}
