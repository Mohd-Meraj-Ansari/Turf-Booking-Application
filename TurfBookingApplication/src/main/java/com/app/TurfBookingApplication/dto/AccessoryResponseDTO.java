package com.app.TurfBookingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AccessoryResponseDTO {

    private Long id;
    private String accessoryName;
    private Double pricePerHour;
    private Integer quantity;
}
