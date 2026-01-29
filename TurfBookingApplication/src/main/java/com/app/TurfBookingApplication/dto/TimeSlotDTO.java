package com.app.TurfBookingApplication.dto;

import java.time.LocalTime;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSlotDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // available | booked
}
