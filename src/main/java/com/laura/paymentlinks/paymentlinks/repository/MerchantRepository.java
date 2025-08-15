package com.laura.paymentlinks.paymentlinks.repository;

import com.laura.paymentlinks.paymentlinks.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByEmail(String email);

}