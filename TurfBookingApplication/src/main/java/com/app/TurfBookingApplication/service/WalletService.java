package com.app.TurfBookingApplication.service;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.WalletResponseDTO;

public interface WalletService {

    WalletResponseDTO addBalance(Double amount, String loggedInEmail);

	WalletResponseDTO getBalance(String email);

	Double getAdminWalletBalance(Authentication authentication);

}

