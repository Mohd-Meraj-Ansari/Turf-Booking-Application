package com.app.TurfBookingApplication.entity;

import java.time.LocalDateTime;

import com.app.TurfBookingApplication.enums.WalletTransactionReason;
import com.app.TurfBookingApplication.enums.WalletTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Wallet wallet;

	private Double amount;

	@Enumerated(EnumType.STRING)
	private WalletTransactionType transactionType;

	@Enumerated(EnumType.STRING)
	private WalletTransactionReason reason;

	@ManyToOne
	private Booking booking;

	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
