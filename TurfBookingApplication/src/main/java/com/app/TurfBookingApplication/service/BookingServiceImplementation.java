package com.app.TurfBookingApplication.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.app.TurfBookingApplication.enums.CancelledBy;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.enums.WalletTransactionReason;
import com.app.TurfBookingApplication.exception.InsufficientBalanceException;
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
    private final WalletTransactionService walletTransactionService;
    
   
    private static final LocalTime PEAK_START_TIME = LocalTime.of(18, 0);
    private static final double PEAK_MULTIPLIER = 1.5; // 50% extra after 6:00 pm
    double discountAmount ;

   
    @Override
    public BookingResponseDTO bookTurf(
            BookingRequestDTO request,
            Authentication authentication) {

        User client = getLoggedInClient(authentication);
        Turf turf = getTurf(request.getTurfId());
        User admin = turf.getOwner();

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
        
        double accessoriesTotal = accessories.stream()
                .mapToDouble(BookingAccessory::getCost)
                .sum();

        double totalAmount =
                turfAmount + accessories.stream().mapToDouble(BookingAccessory::getCost).sum();
        
        // fetch client wallet
        Wallet clientWallet = walletRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Client wallet not found"));

        // calculate discount
        discountAmount =
                calculateDiscount(clientWallet.getBalance(), totalAmount);

        // final payable amount
        double finalAmount = totalAmount - discountAmount;
        
        double advanceAmount =
                transferBookingAmount(client, admin, finalAmount);
        
        Booking booking = saveBooking(
                client,
                turf,
                window,
                bookingType,
                bookingHours,
                bookingDays,
                turfAmount,
                accessoriesTotal,
                advanceAmount,
                discountAmount
                
        );

        saveBookingAccessories(accessories, booking);

        return buildResponse(booking, turf);
    }

   
    //fetch client
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

   
    //booking window
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

   
    //check availability/overlap
    private TurfAvailability validateAvailability(
            Turf turf,
            BookingType type,
            BookingWindow window) {

        TurfAvailability firstDayAvailability = null;

        for (LocalDate date = window.getStartDate();
        	     !date.isAfter(window.getEndDate());
        	     date = date.plusDays(1)) {

        	    final LocalDate bookingDate = date;

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

         //If trying FULL_DAY or MULTI_DAY
            if (type != BookingType.HOURLY) {

                // block if any hourly booking exists
                if (bookingRepository.existsAnyHourlyOnDate(turf, date)) {
                    throw new RuntimeException(
                        "Turf already booked for some hours on " + date
                    );
                }

                // block if FULL_DAY / MULTI_DAY exists
                if (bookingRepository.existsNonHourlyOverlap(
                        turf,
                        date,
                        List.of(BookingType.FULL_DAY, BookingType.MULTI_DAY)
                )) {
                    throw new RuntimeException(
                        "Turf already booked on " + date
                    );
                }
            }

            //If trying HOURLY
            if (type == BookingType.HOURLY) {

                //block HOURLY ↔ HOURLY overlap
                if (bookingRepository.existsHourlyOverlap(
                        turf,
                        date,
                        window.getStartTime(),
                        window.getEndTime()
                )) {
                    throw new RuntimeException(
                        "Time slot already booked on " + date
                    );
                }

                //block HOURLY if FULL_DAY / MULTI_DAY exists
                if (bookingRepository.existsNonHourlyOverlap(
                        turf,
                        date,
                        List.of(BookingType.FULL_DAY, BookingType.MULTI_DAY)
                )) {
                    throw new RuntimeException(
                        "Turf is fully booked on " + date
                    );
                }
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

   
    //calculate days/hours
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
    
    private double calculateDiscount(double walletBalance, double totalAmount) {

        double discountPercentage = walletBalance / 1000;

        // cap discount at 50%
        if (discountPercentage > 50) {
            discountPercentage = 50;
        }

        double discountAmount = (discountPercentage / 100) * totalAmount;

        // safety guard
        if (discountAmount > totalAmount) {
            discountAmount = totalAmount;
        }

        return discountAmount;
    }
   
    //calculate turf amount
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

   
    //accessory
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

 //Transfer booking amount from client to admin
    private double transferBookingAmount(
            User client,
            User admin,
            double totalAmount
    ) {

        Wallet clientWallet = walletRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Client wallet not found"));

        Wallet adminWallet = walletRepository.findByClient(admin)
                .orElseThrow(() -> new RuntimeException("Admin wallet not found"));

        if (clientWallet.getBalance() < totalAmount) {
            throw new InsufficientBalanceException("Insufficient wallet balance");
        }

        // Deduct from client
        clientWallet.setBalance(clientWallet.getBalance() - totalAmount);

        // Credit admin
        adminWallet.setBalance(adminWallet.getBalance() + totalAmount);

        walletRepository.save(clientWallet);
        walletRepository.save(adminWallet);
        
        walletTransactionService.recordTransaction(
                client.getId(),
                totalAmount,
                WalletTransactionReason.BOOKING_PAYMENT,
                "Turf booking payment",
                false, // debit
                clientWallet.getBalance()
        );

        walletTransactionService.recordTransaction(
                admin.getId(),
                totalAmount,
                WalletTransactionReason.BOOKING_PAYMENT,
                "Turf booked by  " + client.getName()+ " (Discount applied ₹" + discountAmount + ")",
                true, // credit
                adminWallet.getBalance()
        );

        return totalAmount; 
    }

    

    //save booking
    private Booking saveBooking(
            User client,
            Turf turf,
            BookingWindow window,
            BookingType bookingType,
            int bookingHours,
            int bookingDays,
            double turfAmount,
            double accessoriesTotal,
            double advanceAmount,
            double discountAmount
    ) {

        Booking booking = Booking.builder()
                .client(client)
                .turf(turf)
                .startDate(window.getStartDate())
                .endDate(window.getEndDate())
                .startTime(window.getStartTime())
                .endTime(window.getEndTime())
                .bookingType(bookingType)
                .totalHours(bookingHours)
                .totalDays(bookingDays)
                .basePrice(turf.getPricePerHour())     
                .accessoriesTotal(accessoriesTotal) 
                .totalAmount(turfAmount + accessoriesTotal)
                .advanceAmount(advanceAmount)
                .status(BookingStatus.BOOKED)
                .discountAmount(discountAmount)
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
                .discountAmount(booking.getDiscountAmount())
                .build();
    }


    //get booking history
    @Override
    public List<BookingResponseDTO> getPastBookings(Authentication authentication) {

        User client = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (client.getRole() != UserRole.CLIENT) {
            throw new RuntimeException("Only clients can view bookings");
        }

        List<Booking> bookings =
                bookingRepository.findAllBookingsByClient(client);

        return bookings.stream()
                .map(b -> BookingResponseDTO.builder()
                        .bookingid(b.getId())
                        .turfId(b.getTurf().getId())
                        .turfName(b.getTurf().getTurfName())
                        .bookingDate(b.getStartDate())
                        .startDate(b.getStartDate())
                        .endDate(b.getEndDate())
                        .startTime(b.getStartTime())
                        .endTime(b.getEndTime())
                        .totalAmount(b.getTotalAmount())
                        .advanceAmount(b.getAdvanceAmount())
                        .status(b.getStatus())
                        .build()
                )
                .toList();
    }

    //cancel booking
    @Override
    public void cancelBooking(Long bookingId, Authentication authentication) {

        User client = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (client.getRole() != UserRole.CLIENT) {
            throw new RuntimeException("Only clients can cancel bookings");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // ownership check
        if (!booking.getClient().getId().equals(client.getId())) {
            throw new RuntimeException("You cannot cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        //time calculation
        LocalDateTime now = LocalDateTime.now();

        LocalTime startTime = booking.getStartTime() != null
                ? booking.getStartTime()
                : LocalTime.MIDNIGHT; // for fullday/multiday

        LocalDateTime bookingStart =
                LocalDateTime.of(booking.getStartDate(), startTime);

        if (bookingStart.isBefore(now)) {
            throw new RuntimeException("Past bookings cannot be cancelled");
        }

        long hoursBeforeStart =
                ChronoUnit.HOURS.between(now, bookingStart);

        //calculate refund
        double advanceAmount = booking.getAdvanceAmount();
        double refundPercentage;

        if (hoursBeforeStart >= 24) {
            refundPercentage = 1.0;     // 100% refund
        } else if (hoursBeforeStart >= 12) {
            refundPercentage = 0.5;     // 50% refund
        } else {
            refundPercentage = 0.4;     // 40% refund
        }

        double refundAmount = advanceAmount * refundPercentage;

        //client wallet refund
        Wallet clientWallet = walletRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Client wallet not found"));

        clientWallet.setBalance(clientWallet.getBalance() + refundAmount);

        //admin wallet deduction
        User admin = booking.getTurf().getOwner();

        Wallet adminWallet = walletRepository.findByClient(admin)
                .orElseThrow(() -> new RuntimeException("Admin wallet not found"));

        if (adminWallet.getBalance() < refundAmount) {
            throw new RuntimeException("Admin has insufficient balance for refund");
        }

        adminWallet.setBalance(adminWallet.getBalance() - refundAmount);

        walletRepository.save(clientWallet);
        walletRepository.save(adminWallet);

        //update booking
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledBy(CancelledBy.CLIENT);
        bookingRepository.save(booking);
        
        walletTransactionService.recordTransaction(
                client.getId(),
                refundAmount,
                WalletTransactionReason.BOOKING_PAYMENT,
                "Refund for cancelled booking",
                true,
                clientWallet.getBalance()
        );

        walletTransactionService.recordTransaction(
                admin.getId(),
                refundAmount,
                WalletTransactionReason.BOOKING_PAYMENT,
                "Refund issued to client",
                false,
                adminWallet.getBalance()
        );
    }


    @Override
    public List<BookingResponseDTO> getBookingsForMyTurf(Authentication authentication) {

        User admin = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can view turf bookings");
        }

        Turf turf = turfRepository.findByOwnerId(admin.getId())
                .orElseThrow(() -> new RuntimeException("Admin has no turf"));

        List<Booking> bookings = bookingRepository.findAllBookingsForTurf(turf);

        return bookings.stream()
                .map(b -> BookingResponseDTO.builder()
                        .bookingid(b.getId())
                        .turfId(turf.getId())
                        .turfName(turf.getTurfName())
                        .clientName(b.getClient().getName())
                        .bookingDate(b.getStartDate())
                        .startDate(b.getStartDate())
                        .endDate(b.getEndDate())
                        .startTime(b.getStartTime())
                        .endTime(b.getEndTime())
                        .totalAmount(b.getTotalAmount())
                        .advanceAmount(b.getAdvanceAmount())
                        .status(b.getStatus())
                        .build())
                .toList();
    }


	@Override
	public Object getAllBookingsForMyTurf(Authentication authentication) {
		// TODO Auto-generated method stub
		return null;
	}


}


