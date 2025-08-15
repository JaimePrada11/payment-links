package com.laura.paymentlinks.paymentlinks.repository;

import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {

    Optional<PaymentAttempt> findByIdempotencyKeyAndPaymentLinkId(String key, Long linkId);
}
