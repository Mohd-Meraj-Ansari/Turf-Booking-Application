package com.app.TurfBookingApplication.entity;

import java.time.LocalDateTime;

import com.app.TurfBookingApplication.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

	
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	
	@PrePersist
	protected void atCreation() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.modifiedAt = now;
	}
	
	@PreUpdate
	protected void atUpdation()
	{
		this.modifiedAt = LocalDateTime.now();
	}
}
