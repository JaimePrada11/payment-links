package com.laura.paymentlinks.paymentlinks.dto;

import java.util.Map;

public record ProblemDetailResponse(
        String type,
        String title,
        int status,
        String detail,
        String code,
        Map<String, Object> errors
) {}