package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.WalletTransactionResponseDTO;
import com.app.TurfBookingApplication.enums.WalletTransactionReason;

public interface WalletTransactionService {

    List<WalletTransactionResponseDTO> getMyWalletTransactions(Authentication authentication);

    void recordTransaction(
            Long userId,
            Double amount,
            WalletTransactionReason reason,
            String description,
            boolean isCredit,
            Double balanceAfter
    );
}
