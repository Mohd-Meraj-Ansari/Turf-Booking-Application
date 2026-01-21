package com.app.TurfBookingApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Optional<Wallet> findByClient(User client);
}

