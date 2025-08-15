package com.laura.paymentlinks.paymentlinks.repository;

import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentLinkRepository extends JpaRepository<PaymentLink, Long> {
    Optional<PaymentLink> findByReference(String reference);

    Optional<PaymentLink> findById(Long id);

    List<PaymentLink> findByMerchant(Merchant merchant);

    List<PaymentLink> findByStatusAndExpiresAtBefore(PaymentLink.PaymentStatus status, LocalDateTime dateTime);
}