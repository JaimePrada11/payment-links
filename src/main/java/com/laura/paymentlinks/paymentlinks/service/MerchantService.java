package com.laura.paymentlinks.paymentlinks.service;

import com.laura.paymentlinks.paymentlinks.dto.MerchantDto;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }


    public MerchantDto findByEmail(String email) {
        if (email == null) {
            throw new NullPointerException("email is null");
        }

        return merchantRepository.findByEmail(email)
                .map( merchant ->  new MerchantDto(
                        merchant.getName(),
                        merchant.getEmail()
                ))
                .orElse(null);
    }

    public Optional<Merchant> findById(Long merchantId) {
        if (merchantId == null) throw new NullPointerException("id is null");
        return merchantRepository.findById(merchantId);
    }

}
