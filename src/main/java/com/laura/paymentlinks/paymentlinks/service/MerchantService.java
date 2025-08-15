package com.laura.paymentlinks.paymentlinks.service;

import com.laura.paymentlinks.paymentlinks.dto.MerchantDto;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository, PasswordEncoder passwordEncoder) {
        this.merchantRepository = merchantRepository;
        this.passwordEncoder = passwordEncoder;
    }


        public Merchant create(Merchant merchant) {
            merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
            return merchantRepository.save(merchant);
        }

        public Optional<Merchant> findById(Long id) {
            return merchantRepository.findById(id);
        }

        public Optional<Merchant> findByEmail(String email) {
            return merchantRepository.findByEmail(email);
        }

        public boolean verificarLogin(String email, String password) {
            Optional<Merchant> userOpt = merchantRepository.findByEmail(email);
            return userOpt.map(user -> passwordEncoder.matches(password, user.getPassword())).orElse(false);
        }

        public boolean existsByEmail(String email) {
            return merchantRepository.findByEmail(email).isPresent();
        }

}
