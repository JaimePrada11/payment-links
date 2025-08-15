package com.laura.paymentlinks.paymentlinks.controller;

import com.laura.paymentlinks.paymentlinks.config.JWTAuthtenticationConfig;
import com.laura.paymentlinks.paymentlinks.dto.AuthDto;
import com.laura.paymentlinks.paymentlinks.dto.TokenResponse;
import com.laura.paymentlinks.paymentlinks.model.Merchant;
import com.laura.paymentlinks.paymentlinks.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final MerchantService merchantService;
    private final JWTAuthtenticationConfig jwtAuthenticationConfig;

    @Autowired
    public AuthController(MerchantService merchantService, JWTAuthtenticationConfig jwtAuthenticationConfig) {
        this.merchantService = merchantService;
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password) {

        if (merchantService.verificarLogin(email, password)) {
            String token = jwtAuthenticationConfig.getJWTToken(email);
            AuthDto user = new AuthDto(email, token);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Merchant merchant) {
        if (merchantService.existsByEmail(merchant.getEmail())) {
            return ResponseEntity.badRequest().body("Merchant already exists.");
        }
        Merchant savedUser = merchantService.create(merchant);
        return ResponseEntity.ok("Usuario registrado exitosamente: " + savedUser.getEmail());
    }
}