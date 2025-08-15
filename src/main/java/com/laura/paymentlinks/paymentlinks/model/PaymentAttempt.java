package com.laura.paymentlinks.paymentlinks.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_link_id", nullable = false)
    private PaymentLink paymentLink;

    @Enumerated(EnumType.STRING)
    private AttemptStatus status;

    private String reason;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AttemptStatus {
        SUCCESS, FAILED
    }
}
