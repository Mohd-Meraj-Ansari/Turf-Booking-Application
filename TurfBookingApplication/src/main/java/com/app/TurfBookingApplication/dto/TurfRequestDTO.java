package com.app.TurfBookingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // generates no-args constructor
@AllArgsConstructor // generates all-args constructor
@Builder // enables builder pattern
public class TurfRequestDTO {

	private String name;
	private String location;
	private Double pricePerHour;
	private Long ownerId; // reference to User (owner)
}
