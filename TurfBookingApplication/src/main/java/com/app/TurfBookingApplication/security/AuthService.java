package com.app.TurfBookingApplication.security;

import com.app.TurfBookingApplication.entity.User;

public interface AuthService {
    Long getLoggedInUserId();

    User getLoggedInUser();

}
