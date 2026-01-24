package com.app.TurfBookingApplication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessoryCostDTO {

    private Long accessoryId;
    private String accessoryName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
