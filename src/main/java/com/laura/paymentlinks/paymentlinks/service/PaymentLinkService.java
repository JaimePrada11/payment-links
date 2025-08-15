package com.laura.paymentlinks.paymentlinks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laura.paymentlinks.paymentlinks.dto.CreatePaymentLinkRequest;
import com.laura.paymentlinks.paymentlinks.dto.*;
import com.laura.paymentlinks.paymentlinks.dto.PaymentLinkResponse;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.model.PaymentAttempt;
import com.laura.paymentlinks.paymentlinks.model.PaymentLink;
import com.laura.paymentlinks.paymentlinks.repository.PaymentAttemptRepository;
import com.laura.paymentlinks.paymentlinks.repository.PaymentLinkRepository;
import com.laura.paymentlinks.paymentlinks.util.Exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PaymentLinkService {

    private final PaymentLinkRepository paymentLinkRepository;
    private final MerchantService merchantService;
    private final ObjectMapper mapper;

    @Autowired
    public PaymentLinkService( PaymentLinkRepository paymentLinkRepository,   MerchantService merchantService,  ObjectMapper mapper) {
        this.paymentLinkRepository = paymentLinkRepository;
        this.merchantService = merchantService;
        this.mapper = mapper;
    }

    @Transactional
    public PaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest req, Merchant merch) {

        Merchant merchant = merchantService.findById(merch.getId())
                .orElseThrow(() -> new RuntimeException("Merchant no encontrado"));

        if( req.amountCents() == null || req.amountCents().intValue() <= 0 ) throw  new ValidationException(" Amount must be greater than 0 ");
        if (!Pattern.matches("^[A-Z]{3}$", req.currency()))
            throw new ValidationException("currency must be ISO 4217 (e.g., COP, USD)");

        PaymentLink link = new PaymentLink();
        link.setMerchant(merchant);
        link.setAmountCents(req.amountCents());
        link.setCurrency(req.currency());
        link.setDescription(req.description());
        link.setStatus(PaymentLink.PaymentStatus.CREATED);
        link.setExpiresAt(LocalDateTime.now().plus(Duration.ofMinutes(req.expiresInMinutes())));
        link.setMetadata(req.metadata());

        paymentLinkRepository.save(link);
        return map(link);
    }

    public PaymentLinkResponse map(PaymentLink l) {
        return new PaymentLinkResponse(
                String.valueOf(l.getId()),
                l.getReference(),
                l.getStatus().name(),
                l.getExpiresAt(),
                l.getAmountCents(),
                l.getCurrency(),
                l.getDescription(),
                l.getPaidAt(),
                l.getMetadata(),
                l.getCreatedAt()
        );
    }


    public PaymentLink find(Long merchantId, String Ref) {
        Long id = null; String ref = null;
        if (Ref.matches("\\d+")) id = Long.valueOf(Ref); else ref = Ref;
        return paymentLinkRepository.findByIdOrReferenceForMerchant(merchantId, id, ref)
                .orElseThrow(() -> new NotFoundException("Payment link not found"));
    }
}
