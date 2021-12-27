package com.danikvitek.CommentService.util.exception;

public class AuthException extends RuntimeException {
    public AuthException() {
        super("Invalid access");
    }
}
