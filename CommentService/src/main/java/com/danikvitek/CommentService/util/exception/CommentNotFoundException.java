package com.danikvitek.CommentService.util.exception;

public final class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("Comment not found");
    }
}
