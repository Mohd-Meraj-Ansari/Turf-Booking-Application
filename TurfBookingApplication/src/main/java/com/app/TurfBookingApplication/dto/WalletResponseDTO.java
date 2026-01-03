package com.app.TurfBookingApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class WalletResponseDTO {

    private Long id;
    private Long clientId;
    private String clientName;   // optional for convenience
    private Double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
