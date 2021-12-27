package com.danikvitek.IdentityService.util.exception;

public final class NoTokenHeaderException extends IllegalArgumentException {
    public NoTokenHeaderException() {
        super("Authorization header with Bearer token must exist");
    }
}
