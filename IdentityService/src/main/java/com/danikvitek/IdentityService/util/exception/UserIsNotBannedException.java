package com.danikvitek.IdentityService.util.exception;

public final class UserIsNotBannedException extends RuntimeException {
    public UserIsNotBannedException() {
        super("User is not banned");
    }
}
