package com.app.TurfBookingApplication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserRequestDTO {

    private String name;
    private String password; 
}

