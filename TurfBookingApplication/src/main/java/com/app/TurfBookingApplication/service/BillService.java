package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.BillResponseDTO;

public interface BillService {
    List<BillResponseDTO> getMyBills(Authentication authentication);
    BillResponseDTO getBillById(Long billId, Authentication authentication);
}
