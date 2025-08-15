package com.laura.paymentlinks.paymentlinks.repository;

import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {

    PaymentAttempt findByPaymentLinkIdAndIdempotencyKey(Long paymentLinkId, String idempotencyKey);
}
