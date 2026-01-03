package com.app.TurfBookingApplication.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"turf_id", "dayOfWeek"}
    )
)
public class TurfAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Turf turf;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private boolean available;

    private LocalTime openTime;
    private LocalTime closeTime;
}