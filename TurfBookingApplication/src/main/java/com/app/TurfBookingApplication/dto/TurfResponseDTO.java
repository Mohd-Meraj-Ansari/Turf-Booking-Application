package com.app.TurfBookingApplication.dto;

import java.util.List;
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
	private String name;
	private String location;
	private Double pricePerHour;

	private Long ownerId;
	private String ownerName;

	private List<Long> bookingIds;
}
