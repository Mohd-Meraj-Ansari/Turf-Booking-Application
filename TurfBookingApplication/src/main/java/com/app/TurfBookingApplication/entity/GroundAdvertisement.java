package com.app.TurfBookingApplication.entity;

import java.time.LocalDate;

import com.app.TurfBookingApplication.enums.AdvertisementDuration;
import com.app.TurfBookingApplication.enums.AdvertisementStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroundAdvertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User advertiser;

    @ManyToOne
    private Turf turf;

    @Enumerated(EnumType.STRING)
    private AdvertisementDuration duration;

    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}

