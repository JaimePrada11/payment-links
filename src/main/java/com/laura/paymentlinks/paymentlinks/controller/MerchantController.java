package com.laura.paymentlinks.paymentlinks.controller;

import com.laura.paymentlinks.paymentlinks.dto.MerchantDto;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    private MerchantService merchantService;

    @Autowired
    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping()
    public ResponseEntity<MerchantDto> getMerchant( @RequestParam String email){
        MerchantDto dto = merchantService.findByEmail(email);

        if (dto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return  ResponseEntity.ok(dto);

    }
}
