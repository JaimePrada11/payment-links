package com.laura.paymentlinks.paymentlinks.service;

import com.laura.paymentlinks.paymentlinks.dto.PaymentLinkResponse;
import com.laura.paymentlinks.paymentlinks.dto.PaymentRequest;
import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.repository.PaymentAttemptRepository;
import com.laura.paymentlinks.paymentlinks.repository.PaymentLinkRepository;
import com.laura.paymentlinks.paymentlinks.util.Exceptions.ConflictException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentLinkRepository paymentLinkRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final PaymentLinkService paymentLinkService;

    @Autowired
    public PaymentService(PaymentAttemptRepository paymentAttemptRepository, PaymentLinkService paymentLinkService,  PaymentLinkRepository paymentLinkRepository) {
        this.paymentAttemptRepository = paymentAttemptRepository;
        this.paymentLinkService = paymentLinkService;
        this.paymentLinkRepository = paymentLinkRepository;
    }

    @Transactional
    public PaymentAttempt pay(Long LinkId, long merchanId, String idemKey, PaymentRequest body){

        if (!StringUtils.hasText(idemKey)) throw new ValidationException("Missing Idempotency-Key");

        PaymentLink link = paymentLinkRepository.findByMerchant(LinkId, merchanId).orElseThrow();

        var existing = paymentAttemptRepository.findByIdempotencyKeyAndPaymentLinkId(idemKey, merchanId);
        if (existing.isPresent()) return  existing.get();

        if (link.getStatus() == PaymentLink.PaymentStatus.PAID
                || link.getStatus()== PaymentLink.PaymentStatus.CANCELLED
                || link.getStatus()== PaymentLink.PaymentStatus.EXPIRED)
            throw  new ConflictException("Link not payable");

        if(link.getExpiresAt().isBefore(LocalDateTime.now()))
            throw  new ConflictException("Link not payable");

        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setPaymentLink(link);
        attempt.setIdempotencyKey(idemKey);

        String token = body.payment_token();

        if (token.startsWith("OK_")){
            link.setStatus(PaymentLink.PaymentStatus.PAID);
            link.setPaidAt(LocalDateTime.now());
            attempt.setStatus(PaymentAttempt.AttemptStatus.SUCCESS);
        } else if (token.startsWith("FAIL_")) {
            attempt.setReason("Simulated failure");
            attempt.setStatus(PaymentAttempt.AttemptStatus.FAILED);
        } else {
            throw new ValidationException("payment_token must start with ok_ or fail_");
        }

        paymentLinkRepository.save(link);
        paymentAttemptRepository.save(attempt);
        return attempt;
    }

    @Transactional
    public  PaymentLink cancel(Long LinkId, long merchanId){

        PaymentLink link = paymentLinkRepository.findByMerchant(LinkId, merchanId).orElseThrow();

        if (link.getStatus()!= PaymentLink.PaymentStatus.CREATED || link.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new ConflictException("Only CREATED and non-expired links can be cancelled");

        PaymentLink before = cloneLink(link);
        link.setStatus(PaymentLink.PaymentStatus.CANCELLED);
        paymentLinkRepository.save(link);
        return link;
    }

    private PaymentLink cloneLink(PaymentLink l) {
        PaymentLink c = new PaymentLink();
        c.setId(l.getId());
        c.setReference(l.getReference());
        c.setStatus(l.getStatus());
        c.setAmountCents(l.getAmountCents());
        c.setCurrency(l.getCurrency());
        c.setDescription(l.getDescription());
        c.setExpiresAt(l.getExpiresAt());
        c.setPaidAt(l.getPaidAt());
        c.setMetadata(l.getMetadata());
        c.setCreatedAt(l.getCreatedAt());
        c.setMerchant(l.getMerchant());
        return c;
    }
}
