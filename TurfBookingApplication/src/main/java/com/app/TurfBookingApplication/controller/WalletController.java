package com.app.TurfBookingApplication.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.WalletResponseDTO;
import com.app.TurfBookingApplication.dto.WalletTransactionResponseDTO;
import com.app.TurfBookingApplication.service.WalletService;
import com.app.TurfBookingApplication.service.WalletTransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletTransactionService walletTransactionService;

    @PostMapping("/add-balance")
    public ResponseEntity<WalletResponseDTO> addBalance(
            @RequestParam Double amount,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        WalletResponseDTO response = walletService.addBalance(amount, loggedInEmail);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/balance")
    public ResponseEntity<WalletResponseDTO> getBalance(Authentication authentication) {
        String email = authentication.getName();
        WalletResponseDTO response = walletService.getBalance(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin-wallet")
    public ResponseEntity<Double> getAdminWalletBalance(Authentication authentication) {
        return ResponseEntity.ok(walletService.getAdminWalletBalance(authentication));
    }

    @GetMapping("/transactions")
    public List<WalletTransactionResponseDTO> getMyTransactions(
            Authentication authentication) {

        return walletTransactionService.getMyWalletTransactions(authentication);
    }
}

