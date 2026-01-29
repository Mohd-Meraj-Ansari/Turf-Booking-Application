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
    private LocalDateTime createdAt;

    private String type;          
    private String reason;      

    private Double amount;
    private Double balanceAfter;

    private Long bookingId;
    private String description;
}
