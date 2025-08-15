package com.laura.paymentlinks.paymentlinks.repository;

import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentLinkRepository extends JpaRepository<PaymentLink, Long> {

    Optional<PaymentLink> findByReference(String reference);

    Optional<PaymentLink> findById(Long id);

    List<PaymentLink> findByMerchant(Merchant merchant);

    List<PaymentLink> findByStatusAndExpiresAtBefore(PaymentLink.PaymentStatus status, LocalDateTime dateTime);

    @Query("SELECT l FROM PaymentLink l WHERE l.merchant.id=:mId")
    List<PaymentLink> search(@Param("mId") Long merchantId);

    @Modifying
    @Query("UPDATE PaymentLink l set l.status='EXPIRED' WHERE l.expiresAt < :now")
    int expiredLinks(@Param("now")LocalDateTime now);
}