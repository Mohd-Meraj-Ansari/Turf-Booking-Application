package com.app.TurfBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

