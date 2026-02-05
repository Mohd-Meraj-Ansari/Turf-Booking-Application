package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.BillResponseDTO;
import com.app.TurfBookingApplication.entity.Bill;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.repository.BillRepository;
import com.app.TurfBookingApplication.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;

    @Override
    public List<BillResponseDTO> getMyBills(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Bill> bills = billRepository.findByUser(user);

        return bills.stream()
                .map(BillResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public BillResponseDTO getBillById(Long billId, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        //Security check: client can view only their bill
        if (!bill.getBooking().getClient().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to view this bill");
        }

        return BillResponseDTO.fromEntity(bill);
    }
}
