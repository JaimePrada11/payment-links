package com.laura.paymentlinks.paymentlinks.util;

import com.laura.paymentlinks.paymentlinks.dto.ProblemDetailResponse;

import java.util.Map;

public class ProblemDetailFactory {

    public static ProblemDetailResponse of(int status, String title, String detail, String code, Map<String,Object> errors) {
        String type = switch (status) {
            case 401 -> "https://errors.example.com/unauthorized";
            case 403 -> "https://errors.example.com/forbidden";
            case 404 -> "https://errors.example.com/not_found";
            case 409 -> "https://errors.example.com/conflict";
            case 422 -> "https://errors.example.com/validation_error";
            default -> "https://errors.example.com/internal_error";
        };
        return new ProblemDetailResponse(type, title, status, detail, code, errors);
    }
}