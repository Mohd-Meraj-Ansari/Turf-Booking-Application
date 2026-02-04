package com.app.TurfBookingApplication.entity;

import java.time.LocalDate;

import com.app.TurfBookingApplication.enums.BillStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bills")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private double baseAmount;        // turf price before discount
    private double accessoriesAmount;
    private double discountAmount;
    private double totalAmount;        // final payable amount

    private LocalDate billDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status;         // PAID, REFUNDED, CANCELLED
}

