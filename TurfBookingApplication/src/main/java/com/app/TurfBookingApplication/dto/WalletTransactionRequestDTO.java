package com.app.TurfBookingApplication.dto;

import com.app.TurfBookingApplication.enums.WalletTransactionReason;
import com.app.TurfBookingApplication.enums.WalletTransactionType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class WalletTransactionRequestDTO {

    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Transaction type is required")
    private WalletTransactionType transactionType;

    @NotNull(message = "Transaction reason is required")
    private WalletTransactionReason reason;

    private Long bookingId; // optional, only if linked to a booking
}
