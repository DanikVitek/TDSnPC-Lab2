package com.danikvitek.IdentityService.util.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public final class UserNotFoundException extends UsernameNotFoundException {
    public UserNotFoundException() {
        super("User not found");
    }
}
