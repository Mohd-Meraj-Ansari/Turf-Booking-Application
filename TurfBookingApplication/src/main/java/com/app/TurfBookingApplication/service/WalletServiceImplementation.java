package com.app.TurfBookingApplication.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.TurfBookingApplication.dto.WalletResponseDTO;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.Wallet;
import com.app.TurfBookingApplication.entity.WalletTransaction;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.enums.WalletTransactionReason;
import com.app.TurfBookingApplication.enums.WalletTransactionType;
import com.app.TurfBookingApplication.repository.UserRepository;
import com.app.TurfBookingApplication.repository.WalletRepository;
import com.app.TurfBookingApplication.repository.WalletTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImplementation implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    public WalletResponseDTO addBalance(Double amount, String loggedInEmail) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        // Fetch User
        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only clients have wallets
        if (user.getRole() != UserRole.CLIENT) {
            throw new RuntimeException("Only clients can have wallets");
        }

        // Fetch wallet
        Wallet wallet = walletRepository.findByClient(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Update balance
        wallet.setBalance(wallet.getBalance() + amount);
        Wallet updatedWallet = walletRepository.save(wallet);

        // Create transaction record
        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(updatedWallet)
                .amount(amount)
                .transactionType(WalletTransactionType.CREDIT)
                .reason(WalletTransactionReason.TOP_UP)
                .build();

        walletTransactionRepository.save(transaction);

        return WalletResponseDTO.builder()
                .walletId(updatedWallet.getId())
                .clientId(user.getId())
                .clientName(user.getName())  
                .balance(updatedWallet.getBalance())
                .message("Wallet updated successfully")
                .build();
    }

    @Override
    public WalletResponseDTO getBalance(String loggedInEmail) {

        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByClient(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return WalletResponseDTO.builder()
                .walletId(wallet.getId())
                .clientId(user.getId())
                .clientName(user.getName())
                .balance(wallet.getBalance())
                .build();
    }

    
}

