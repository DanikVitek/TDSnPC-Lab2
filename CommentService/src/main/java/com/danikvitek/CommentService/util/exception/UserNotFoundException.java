package com.danikvitek.CommentService.util.exception;


public final class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
