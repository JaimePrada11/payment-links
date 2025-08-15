package com.laura.paymentlinks.paymentlinks.model;

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
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "paymentLink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentAttempt> paymentAttempts = new ArrayList<>();

    public PaymentLink(Merchant merchant, String reference, Long amountCents, String currency, LocalDateTime expiresAt) {
        this.merchant = merchant;
        this.reference = reference;
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

    public enum PaymentStatus {
        CREATED, PAID, CANCELLED, EXPIRED
    }
}
