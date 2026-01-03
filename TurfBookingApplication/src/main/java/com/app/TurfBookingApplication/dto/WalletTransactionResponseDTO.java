package com.app.TurfBookingApplication.dto;

import java.time.LocalDateTime;

import com.app.TurfBookingApplication.enums.WalletTransactionReason;
import com.app.TurfBookingApplication.enums.WalletTransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class WalletTransactionResponseDTO {

    private Long id;
    private Long walletId;
    private Double amount;
    private WalletTransactionType transactionType;
    private WalletTransactionReason reason;
    private Long bookingId;
    private LocalDateTime createdAt;
}
