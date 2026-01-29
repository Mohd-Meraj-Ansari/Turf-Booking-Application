package com.app.TurfBookingApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.WalletTransaction;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

	List<WalletTransaction> findByUserOrderByCreatedAtDesc(User user);
}

