package com.app.TurfBookingApplication.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class WalletRequestDTO {

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private Double balance;
}
