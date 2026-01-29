package com.app.TurfBookingApplication.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.WalletTransactionResponseDTO;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.WalletTransaction;
import com.app.TurfBookingApplication.enums.WalletTransactionReason;
import com.app.TurfBookingApplication.enums.WalletTransactionType;
import com.app.TurfBookingApplication.repository.UserRepository;
import com.app.TurfBookingApplication.repository.WalletTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public List<WalletTransactionResponseDTO> getMyWalletTransactions(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(tx -> WalletTransactionResponseDTO.builder()
                		.id(tx.getId())
                        .createdAt(tx.getCreatedAt())
                        .type(tx.getTransactionType().name())
                        .reason(tx.getReason().name())
                        .amount(tx.getAmount())
                        .balanceAfter(tx.getBalanceAfter())
                        .bookingId(tx.getBooking() != null ? tx.getBooking().getId() : null)
                        .description(tx.getDescription())
                        .build()
                )
                .toList();
    } 

    @Override
    public void recordTransaction(
            Long userId,
            Double amount,
            WalletTransactionReason reason,
            String description,
            boolean isCredit,
            Double balanceAfter
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalletTransaction tx = WalletTransaction.builder()
                .user(user)
                .amount(amount)
                .transactionType(
                        isCredit ? WalletTransactionType.CREDIT : WalletTransactionType.DEBIT
                )
                .reason(reason)
                .balanceAfter(balanceAfter)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);
    }
}
