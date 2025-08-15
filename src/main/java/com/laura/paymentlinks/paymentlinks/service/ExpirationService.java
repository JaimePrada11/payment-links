package com.laura.paymentlinks.paymentlinks.service;

import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.repository.PaymentLinkRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpirationService {

    private static final Logger log = LoggerFactory.getLogger(ExpirationService.class);
    private final PaymentLinkRepository paymentLinkRepository;

    public ExpirationService(PaymentLinkRepository paymentLinkRepository) {
        this.paymentLinkRepository = paymentLinkRepository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void expireLinks() {
        LocalDateTime now = LocalDateTime.now();

        List<PaymentLink> toExpire = paymentLinkRepository
                .findByStatusAndExpiresAtBefore(PaymentLink.PaymentStatus.CREATED, now);

        for (PaymentLink link : toExpire) {
            link.setStatus(PaymentLink.PaymentStatus.EXPIRED);
        }

        if (!toExpire.isEmpty()) {
            paymentLinkRepository.saveAll(toExpire);
        }
    }


}