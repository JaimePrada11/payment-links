package com.laura.paymentlinks.paymentlinks.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.laura.paymentlinks.paymentlinks.dto.PaymentLinkDto;
import com.laura.paymentlinks.paymentlinks.util.CurrencyConverter;
import com.laura.paymentlinks.paymentlinks.util.JsonNodeConverter;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(name = "amount_cents", nullable = false)
    private Long amountCents;

    @Column(nullable = false, length = 3)
    @Convert(converter = CurrencyConverter.class)
    private String currency;

    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.CREATED;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now()   ;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode metadata;

    @OneToMany(mappedBy = "paymentLink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentAttempt> paymentAttempts = new ArrayList<>();

    public enum PaymentStatus {
        CREATED, PAID, CANCELLED, EXPIRED
    }

    public PaymentLink(Merchant merchant, Long amountCents, String currency, LocalDateTime expiresAt) {
        this.merchant = merchant;
        this.reference = generateReference();
        this.amountCents = amountCents;
        this.currency = currency;
        this.expiresAt = expiresAt;
        this.paymentAttempts = new ArrayList<>();
    }

    public void addPaymentAttempt(PaymentAttempt attempt) {
        paymentAttempts.add(attempt);
        attempt.setPaymentLink(this);
    }

    public void removePaymentAttempt(PaymentAttempt attempt) {
        paymentAttempts.remove(attempt);
        attempt.setPaymentLink(null);
    }



    private String generateReference() {
        return "PL-2025-" + String.format("%06d", System.currentTimeMillis() % 1000000);
    }

    public boolean isPaid() { return status == status.PAID; }

    public boolean isExpired() { return status == status.EXPIRED || LocalDateTime.now().isAfter(expiresAt); }

    public boolean isCancelled() {
        return status == status.CANCELLED; }


    public PaymentLinkDto toDto() {

        String reference = this.reference;
        Long amountCents = this.amountCents;
        String currency = this.currency;
        String description = this.description;
        PaymentStatus status = this.status;
        LocalDateTime expiresAt = this.expiresAt;
        LocalDateTime paidAt = this.paidAt;
        LocalDateTime createdAt = this.createdAt;

        return new PaymentLinkDto( reference, amountCents,  currency, description, status, expiresAt, paidAt, createdAt);
    }

    public static PaymentLink fromDto(PaymentLinkDto dto, Merchant merchant) {
        PaymentLink link = new PaymentLink(
                merchant,
                dto.amountCents(),
                dto.currency(),
                dto.expiresAt()
        );
        link.setDescription(dto.description());
        if (dto.status() != null) {
            link.setStatus(dto.status());
        }
        link.setPaidAt(dto.paidAt());
        return link;
    }

}
