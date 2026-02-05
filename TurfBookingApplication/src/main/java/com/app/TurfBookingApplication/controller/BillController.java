package com.app.TurfBookingApplication.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.BillResponseDTO;
import com.app.TurfBookingApplication.service.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    //client all bills
    @GetMapping
    public List<BillResponseDTO> getMyBills(Authentication authentication) {
        return billService.getMyBills(authentication);
    }

    //single bill view
    @GetMapping("/{billId}")
    public BillResponseDTO getBillById(
            @PathVariable Long billId,
            Authentication authentication
    ) {
        return billService.getBillById(billId, authentication);
    }
}
