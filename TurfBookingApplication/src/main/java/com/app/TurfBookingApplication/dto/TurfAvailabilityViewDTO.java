package com.app.TurfBookingApplication.dto;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TurfAvailabilityViewDTO {

    private String turfName;
    private LocalDate date;
    private String dayOfWeek;

    private boolean open;
    private LocalTime openTime;
    private LocalTime closeTime;

    private List<TimeSlotDTO> slots;
}