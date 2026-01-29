package com.app.TurfBookingApplication.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.TimeSlotDTO;
import com.app.TurfBookingApplication.dto.TurfAvailabilityViewDTO;
import com.app.TurfBookingApplication.entity.Booking;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.TurfAvailability;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.enums.BookingType;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.repository.BookingRepository;
import com.app.TurfBookingApplication.repository.TurfAvailabilityRepository;
import com.app.TurfBookingApplication.repository.TurfRepository;
import com.app.TurfBookingApplication.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurfAvailabilityServiceImpl implements TurfAvailabilityService {

    private final UserRepository userRepository;
    private final TurfRepository turfRepository;
    private final TurfAvailabilityRepository turfAvailabilityRepository;
    private final BookingRepository bookingRepository;

    @Override
    public TurfAvailabilityViewDTO getAvailability(
            LocalDate date,
            Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Turf turf;

        if (user.getRole() == UserRole.ADMIN) {
            turf = turfRepository.findByOwnerId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Admin has no turf"));
        } else {
            turf = turfRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new RuntimeException("No turf available"));
        }

        TurfAvailability availability =
                turfAvailabilityRepository
                        .findByTurfAndDayOfWeek(turf, date.getDayOfWeek())
                        .orElseThrow(() -> new RuntimeException("Availability not set"));

        // CLOSED DAY
        if (!availability.isAvailable()) {
            return TurfAvailabilityViewDTO.builder()
                    .turfName(turf.getTurfName())
                    .date(date)
                    .dayOfWeek(date.getDayOfWeek().name())
                    .open(false)
                    .slots(List.of())
                    .build();
        }

        // Check FULL_DAY / MULTI_DAY booking
        boolean fullBooked =
                bookingRepository.existsNonHourlyOverlap(
                        turf,
                        date,
                        List.of(BookingType.FULL_DAY, BookingType.MULTI_DAY)
                );

        List<TimeSlotDTO> slots = generateSlots(
                availability.getOpenTime(),
                availability.getCloseTime()
        );

        if (fullBooked) {
            slots.forEach(s -> s.setStatus("BOOKED"));
        } else {
            markHourlyBookings(slots, turf, date);
        }

        return TurfAvailabilityViewDTO.builder()
                .turfName(turf.getTurfName())
                .date(date)
                .dayOfWeek(date.getDayOfWeek().name())
                .open(true)
                .openTime(availability.getOpenTime())
                .closeTime(availability.getCloseTime())
                .slots(slots)
                .build();
    }

    // ---------------- HELPER METHODS ----------------

    private List<TimeSlotDTO> generateSlots(LocalTime open, LocalTime close) {
        List<TimeSlotDTO> slots = new ArrayList<>();
        LocalTime cursor = open;

        while (cursor.isBefore(close)) {
            LocalTime end = cursor.plusHours(1);
            slots.add(new TimeSlotDTO(cursor, end, "AVAILABLE"));
            cursor = end;
        }
        return slots;
    }

    private void markHourlyBookings(
            List<TimeSlotDTO> slots,
            Turf turf,
            LocalDate date) {

        List<Booking> hourlyBookings =
                bookingRepository.findHourlyBookings(turf, date);

        for (Booking booking : hourlyBookings) {
            for (TimeSlotDTO slot : slots) {
                boolean overlap =
                        booking.getStartTime().isBefore(slot.getEndTime()) &&
                        booking.getEndTime().isAfter(slot.getStartTime());

                if (overlap) {
                    slot.setStatus("BOOKED");
                }
            }
        }
    }
}
