package com.laura.paymentlinks.paymentlinks.service;

import com.laura.paymentlinks.paymentlinks.dto.PaymentLinkDto;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.repository.PaymentAttemptRepository;
import com.laura.paymentlinks.paymentlinks.repository.PaymentLinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentLinkService {
    private final PaymentLinkRepository paymentLinkRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final MerchantService merchantService;

    @Autowired
    public PaymentLinkService( PaymentLinkRepository paymentLinkRepository, PaymentAttemptRepository paymentAttemptRepository,  MerchantService merchantService) {
        this.paymentLinkRepository = paymentLinkRepository;
        this.paymentAttemptRepository = paymentAttemptRepository;
        this.merchantService = merchantService;
    };

    @Transactional
    public PaymentLink createPaymentLink(PaymentLinkDto dto, Long merchantId) {
       Merchant merchant = merchantService.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant no encontrado"));

        PaymentLink paymentLink = PaymentLink.fromDto(dto, merchant);

        return paymentLinkRepository.save(paymentLink);
    }


    @Transactional
    public PaymentLink processPayment(Long paymentLinkId, String idempotencyKey, String paymentToken) {

        PaymentLink link = paymentLinkRepository.findById(paymentLinkId)
                .orElseThrow(() -> new RuntimeException("PaymentLink no existe"));

        Optional<PaymentAttempt> existingAttempt = paymentAttemptRepository
                .findByPaymentLinkIdAndIdempotencyKey(paymentLinkId, idempotencyKey);

        if (existingAttempt.isPresent()) {
            return existingAttempt.get().getPaymentLink();
        }

        if (link.getStatus() != PaymentLink.PaymentStatus.CREATED || link.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede pagar este link");
        }

        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setPaymentLink(link);
        attempt.setIdempotencyKey(idempotencyKey);

        if (paymentToken.startsWith("ok_")) {
            attempt.setStatus(PaymentAttempt.AttemptStatus.SUCCESS);
            link.setStatus(PaymentLink.PaymentStatus.PAID);
            link.setPaidAt(LocalDateTime.now());
        } else {
            attempt.setStatus(PaymentAttempt.AttemptStatus.FAILED);
            attempt.setReason("Pago fallido");
        }

        link.getPaymentAttempts().add(attempt);
        paymentLinkRepository.save(link);

        return link;
    }



}
