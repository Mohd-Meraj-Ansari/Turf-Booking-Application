package com.app.TurfBookingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurfResponseDTO {

	  private Long id;
	    private String turfName;
	    private String location;
	    private Double pricePerHour;
	    private Long ownerId;

}
