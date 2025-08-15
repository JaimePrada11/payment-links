package com.laura.paymentlinks.paymentlinks.util.Exceptions;

import com.laura.paymentlinks.paymentlinks.dto.ProblemDetailResponse;
import com.laura.paymentlinks.paymentlinks.util.ProblemDetailFactory;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetailResponse> handleConstraint(ConstraintViolationException ex) {
        return ResponseEntity.status(422).body(
                ProblemDetailFactory.of(422, "Validation Error",
                        ex.getMessage(), "VALIDATION_ERROR", Map.of("violations", ex.getConstraintViolations().toString())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetailResponse> handleInvalid(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream().collect(
                java.util.stream.Collectors.groupingBy(f -> f.getField(),
                        java.util.stream.Collectors.mapping(e -> e.getDefaultMessage(), java.util.stream.Collectors.toList())));
        return ResponseEntity.status(422).body(
                ProblemDetailFactory.of(422, "Validation Error","Invalid body", "VALIDATION_ERROR", Map.of("errors", errors)));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetailResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(
                ProblemDetailFactory.of(404, "Not Found", ex.getMessage(), "NOT_FOUND", null));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetailResponse> handleConflict(ConflictException ex) {
        return ResponseEntity.status(409).body(
                ProblemDetailFactory.of(409, "Conflict", ex.getMessage(), "CONFLICT", null));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetailResponse> handleVal(ValidationException ex) {
        return ResponseEntity.status(422).body(
                ProblemDetailFactory.of(422, "Validation Error", ex.getMessage(), "VALIDATION_ERROR", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetailResponse> handleAny(Exception ex) {
        return ResponseEntity.status(500).body(
                ProblemDetailFactory.of(500, "Internal Error", "Unexpected error", "INTERNAL_ERROR", null));
    }
}
