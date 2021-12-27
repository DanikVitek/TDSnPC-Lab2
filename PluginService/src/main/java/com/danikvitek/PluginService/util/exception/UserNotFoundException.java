package com.danikvitek.PluginService.util.exception;

public final class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
