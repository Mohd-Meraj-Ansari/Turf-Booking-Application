package com.app.TurfBookingApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.TurfBookingApplication.dto.WalletResponseDTO;
import com.app.TurfBookingApplication.service.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/add-balance")
    public ResponseEntity<WalletResponseDTO> addBalance(
            @RequestParam Double amount,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        WalletResponseDTO response = walletService.addBalance(amount, loggedInEmail);

        return ResponseEntity.ok(response);
    }
}

