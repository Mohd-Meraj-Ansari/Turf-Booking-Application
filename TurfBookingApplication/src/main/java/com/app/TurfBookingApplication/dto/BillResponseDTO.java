package com.app.TurfBookingApplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.app.TurfBookingApplication.entity.Bill;
import com.app.TurfBookingApplication.entity.Booking;
import com.app.TurfBookingApplication.enums.BillStatus;
import com.app.TurfBookingApplication.enums.BookingType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponseDTO {

    private Long id;
    // Turf / booking info
    private String turfName;
    private BookingType bookingType;
    private LocalDate bookingDate;
    
    //time
    private LocalTime startTime;
    private LocalTime endTime;

    //date
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Amounts
    private double baseAmount;
    private double accessoriesAmount;
    private double discountAmount;
    private double totalAmount;

    // Status
    private BillStatus status;
    
    public static BillResponseDTO fromEntity(Bill bill) {
    	Booking booking = bill.getBooking();
    	return BillResponseDTO.builder()
                .id(bill.getId())
                .turfName(booking.getTurf().getTurfName())
                .bookingType(booking.getBookingType())
                .bookingDate(booking.getStartDate())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .baseAmount(bill.getBaseAmount())
                .accessoriesAmount(bill.getAccessoriesAmount())
                .discountAmount(bill.getDiscountAmount())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .build();
    }

}