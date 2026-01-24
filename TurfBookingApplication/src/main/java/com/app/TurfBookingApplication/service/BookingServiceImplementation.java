package com.app.TurfBookingApplication.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.BookingAccessoryRequestDTO;
import com.app.TurfBookingApplication.dto.BookingRequestDTO;
import com.app.TurfBookingApplication.dto.BookingResponseDTO;
import com.app.TurfBookingApplication.entity.Accessory;
import com.app.TurfBookingApplication.entity.Booking;
import com.app.TurfBookingApplication.entity.BookingAccessory;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.TurfAvailability;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.Wallet;
import com.app.TurfBookingApplication.enums.BookingStatus;
import com.app.TurfBookingApplication.enums.BookingType;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.repository.AccessoryRepository;
import com.app.TurfBookingApplication.repository.BookingAccessoryRepository;
import com.app.TurfBookingApplication.repository.BookingRepository;
import com.app.TurfBookingApplication.repository.TurfAvailabilityRepository;
import com.app.TurfBookingApplication.repository.TurfRepository;
import com.app.TurfBookingApplication.repository.UserRepository;
import com.app.TurfBookingApplication.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImplementation implements BookingService {

    private final UserRepository userRepository;
    private final TurfRepository turfRepository;
    private final TurfAvailabilityRepository turfAvailabilityRepository;
    private final BookingRepository bookingRepository;
    private final AccessoryRepository accessoryRepository;
    private final BookingAccessoryRepository bookingAccessoryRepository;
    private final WalletRepository walletRepository;

    private static final LocalTime PEAK_START_TIME = LocalTime.of(18, 0);
    private static final double PEAK_MULTIPLIER = 1.5; // ✅ 50% extra

    /* =====================================================
       PUBLIC API
    ===================================================== */

    @Override
    public BookingResponseDTO bookTurf(
            BookingRequestDTO request,
            Authentication authentication) {

        User client = getLoggedInClient(authentication);
        Turf turf = getTurf(request.getTurfId());

        BookingType bookingType = request.getBookingType();
        BookingWindow window = resolveBookingWindow(request);

        TurfAvailability availability =
                validateAvailability(turf, bookingType, window);

        int bookingDays = calculateBookingDays(bookingType, window);
        int bookingHours = calculateBookingHours(bookingType, window, availability);

        double turfAmount =
                calculateTurfAmount(
                        bookingType, turf, availability, window, bookingDays);

        List<BookingAccessory> accessories =
                calculateAccessories(request, bookingHours);

        double totalAmount =
                turfAmount + accessories.stream().mapToDouble(BookingAccessory::getCost).sum();

        double advanceAmount =
                processWalletAdvance(client, totalAmount);

        Booking booking =
                saveBooking(client, turf, window, bookingType,
                        bookingHours, totalAmount, advanceAmount);

        saveBookingAccessories(accessories, booking);

        return buildResponse(booking, turf);
    }

    /* =====================================================
       AUTH & BASIC FETCH
    ===================================================== */

    private User getLoggedInClient(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.CLIENT)
            throw new RuntimeException("Only clients can book turf");

        return user;
    }

    private Turf getTurf(Long turfId) {
        return turfRepository.findById(turfId)
                .orElseThrow(() -> new RuntimeException("Turf not found"));
    }

    /* =====================================================
       BOOKING WINDOW
    ===================================================== */

    @Getter
    @AllArgsConstructor
    private static class BookingWindow {
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    private BookingWindow resolveBookingWindow(BookingRequestDTO request) {

        BookingType type = request.getBookingType();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (type == BookingType.MULTI_DAY) {

            if (request.getStartDate() == null || request.getEndDate() == null)
                throw new RuntimeException("Start and end date required");

            if (request.getStartDate().isBefore(today))
                throw new RuntimeException("Cannot book turf for past dates");

            if (request.getEndDate().isBefore(request.getStartDate()))
                throw new RuntimeException("End date cannot be before start date");

            return new BookingWindow(
                    request.getStartDate(),
                    request.getEndDate(),
                    null,
                    null
            );
        }

        if (type == BookingType.HOURLY) {

            if (request.getBookingDate() == null ||
                request.getStartTime() == null ||
                request.getEndTime() == null)
                throw new RuntimeException("Date, startTime and endTime required");

            if (request.getBookingDate().isBefore(today))
                throw new RuntimeException("Cannot book turf for past dates");

            if (request.getBookingDate().isEqual(today) &&
                request.getStartTime().isBefore(now))
                throw new RuntimeException("Cannot book past time slot");

            if (!request.getEndTime().isAfter(request.getStartTime()))
                throw new RuntimeException("End time must be after start time");

            return new BookingWindow(
                    request.getBookingDate(),
                    request.getBookingDate(),
                    request.getStartTime(),
                    request.getEndTime()
            );
        }

        if (request.getBookingDate() == null)
            throw new RuntimeException("Booking date required");

        if (request.getBookingDate().isBefore(today))
            throw new RuntimeException("Cannot book turf for past dates");

        return new BookingWindow(
                request.getBookingDate(),
                request.getBookingDate(),
                null,
                null
        );
    }

    /* =====================================================
       AVAILABILITY & OVERLAP
    ===================================================== */

    private TurfAvailability validateAvailability(
            Turf turf,
            BookingType type,
            BookingWindow window) {

        TurfAvailability firstDayAvailability = null;

        for (LocalDate date = window.getStartDate();
        	     !date.isAfter(window.getEndDate());
        	     date = date.plusDays(1)) {

        	    final LocalDate bookingDate = date; // ✅ make it effectively final

        	    TurfAvailability availability =
        	            turfAvailabilityRepository
        	                    .findByTurfAndDayOfWeek(turf, bookingDate.getDayOfWeek())
        	                    .orElseThrow(() ->
        	                            new RuntimeException(
        	                                    "Turf not available on " + bookingDate));

        	    if (!availability.isAvailable())
        	        throw new RuntimeException("Turf closed on " + bookingDate);


            if (firstDayAvailability == null)
                firstDayAvailability = availability;

            if (type != BookingType.HOURLY &&
                bookingRepository.existsNonHourlyOverlap(
                        turf,
                        date,
                        List.of(BookingType.FULL_DAY, BookingType.MULTI_DAY))) {
                throw new RuntimeException("Turf already booked on " + date);
            }

            if (type == BookingType.HOURLY &&
                bookingRepository.existsHourlyOverlap(
                        turf,
                        date,
                        window.getStartTime(),
                        window.getEndTime())) {
                throw new RuntimeException("Time slot already booked on " + date);
            }
        }

        return firstDayAvailability;
    }

    /* =====================================================
       DAYS & HOURS
    ===================================================== */

    private int calculateBookingDays(
            BookingType type,
            BookingWindow window) {

        if (type == BookingType.HOURLY || type == BookingType.FULL_DAY)
            return 1;

        return (int) ChronoUnit.DAYS.between(
                window.getStartDate(),
                window.getEndDate()) + 1;
    }

    private int calculateBookingHours(
            BookingType type,
            BookingWindow window,
            TurfAvailability availability) {

        if (type == BookingType.HOURLY) {
            return (int) Duration.between(
                    window.getStartTime(),
                    window.getEndTime()).toHours();
        }

        int dailyHours = (int) Duration.between(
                availability.getOpenTime(),
                availability.getCloseTime()).toHours();

        if (type == BookingType.FULL_DAY)
            return dailyHours;

        return dailyHours * calculateBookingDays(type, window);
    }

    /* =====================================================
       PRICING (50% PEAK AFTER 6 PM)
    ===================================================== */

    private double calculateTurfAmount(
            BookingType type,
            Turf turf,
            TurfAvailability availability,
            BookingWindow window,
            int bookingDays) {

        if (type == BookingType.HOURLY) {
            return calculateTurfPrice(
                    window.getStartTime(),
                    window.getEndTime(),
                    turf.getPricePerHour());
        }

        double dailyPrice = calculateTurfPrice(
                availability.getOpenTime(),
                availability.getCloseTime(),
                turf.getPricePerHour());

        return type == BookingType.FULL_DAY
                ? dailyPrice
                : dailyPrice * bookingDays;
    }

    private double calculateTurfPrice(
            LocalTime start,
            LocalTime end,
            double pricePerHour) {

        double total = 0;
        LocalTime cursor = start;

        while (cursor.isBefore(end)) {
            boolean isPeak = !cursor.isBefore(PEAK_START_TIME);
            total += isPeak
                    ? pricePerHour * PEAK_MULTIPLIER
                    : pricePerHour;
            cursor = cursor.plusHours(1);
        }
        return total;
    }

    /* =====================================================
       ACCESSORIES
    ===================================================== */

    private List<BookingAccessory> calculateAccessories(
            BookingRequestDTO request,
            int bookingHours) {

        List<BookingAccessory> result = new ArrayList<>();

        if (request.getAccessories() == null)
            return result;

        for (BookingAccessoryRequestDTO dto : request.getAccessories()) {

            Accessory accessory = accessoryRepository.findById(dto.getAccessoryId())
                    .orElseThrow(() -> new RuntimeException("Accessory not found"));

            if (dto.getQuantity() > accessory.getQuantity())
                throw new RuntimeException("Accessory quantity exceeds allowed limit");

            double cost = accessory.getPricePerHour()
                    * dto.getQuantity()
                    * bookingHours;

            result.add(
                    BookingAccessory.builder()
                            .accessory(accessory)
                            .quantity(dto.getQuantity())
                            .hours(bookingHours)
                            .cost(cost)
                            .build()
            );
        }

        return result;
    }

    private void saveBookingAccessories(
            List<BookingAccessory> accessories,
            Booking booking) {

        for (BookingAccessory ba : accessories) {
            ba.setBooking(booking);
            bookingAccessoryRepository.save(ba);
        }
    }

    /* =====================================================
       WALLET
    ===================================================== */

    private double processWalletAdvance(User client, double totalAmount) {

        double advanceAmount = totalAmount * 0.30;

        Wallet wallet = walletRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < advanceAmount)
            throw new RuntimeException("Insufficient wallet balance");

        wallet.setBalance(wallet.getBalance() - advanceAmount);
        walletRepository.save(wallet);

        return advanceAmount;
    }

    /* =====================================================
       BOOKING & RESPONSE
    ===================================================== */

    private Booking saveBooking(
            User client,
            Turf turf,
            BookingWindow window,
            BookingType type,
            int bookingHours,
            double totalAmount,
            double advanceAmount) {

        Booking booking = Booking.builder()
                .client(client)
                .turf(turf)
                .startDate(window.getStartDate())
                .endDate(window.getEndDate())
                .startTime(window.getStartTime())
                .endTime(window.getEndTime())
                .bookingType(type)
                .totalHours(bookingHours)
                .totalAmount(totalAmount)
                .advanceAmount(advanceAmount)
                .status(BookingStatus.BOOKED)
                .build();

        return bookingRepository.save(booking);
    }

    private BookingResponseDTO buildResponse(
            Booking booking,
            Turf turf) {

        LocalDate bookingDate = booking.getStartDate();

        LocalTime startTime = booking.getStartTime();
        LocalTime endTime = booking.getEndTime();

        if (startTime == null || endTime == null) {

            TurfAvailability availability =
                    turfAvailabilityRepository
                            .findByTurfAndDayOfWeek(
                                    turf,
                                    bookingDate.getDayOfWeek())
                            .orElseThrow(() ->
                                    new RuntimeException("Availability not found"));

            startTime = availability.getOpenTime();
            endTime = availability.getCloseTime();
        }

        return BookingResponseDTO.builder()
                .bookingid(booking.getId())
                .turfId(turf.getId())
                .turfName(turf.getTurfName())
                .bookingDate(bookingDate)
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .startTime(startTime)
                .endTime(endTime)
                .totalAmount(booking.getTotalAmount())
                .advanceAmount(booking.getAdvanceAmount())
                .status(booking.getStatus())
                .build();
    }
}
