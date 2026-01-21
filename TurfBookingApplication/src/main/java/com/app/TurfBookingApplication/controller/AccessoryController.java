package com.app.TurfBookingApplication.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.AccessoryRequestDTO;
import com.app.TurfBookingApplication.dto.AccessoryResponseDTO;
import com.app.TurfBookingApplication.service.AccessoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;

    @PostMapping("/add")
    public ResponseEntity<AccessoryResponseDTO> addAccessory(
            @RequestBody AccessoryRequestDTO request,
            Authentication authentication) {

        return ResponseEntity.ok(accessoryService.addAccessory(request, authentication));
    }

    @GetMapping("/turf")
    public ResponseEntity<List<AccessoryResponseDTO>> getAccessoriesForOwner(
            Authentication authentication) {

        return ResponseEntity.ok(accessoryService.getAccessoriesForOwner(authentication));
    }
    
    @PostMapping("/add-multiple")
    public ResponseEntity<List<AccessoryResponseDTO>> addMultiple(
            @RequestBody List<AccessoryRequestDTO> accessories,
            Authentication authentication) {

        return ResponseEntity.ok(accessoryService.addMultipleAccessories(accessories, authentication));
    }
}

