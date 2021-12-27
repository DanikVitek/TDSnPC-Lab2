package com.danikvitek.AuthService.util.exception;

public final class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
