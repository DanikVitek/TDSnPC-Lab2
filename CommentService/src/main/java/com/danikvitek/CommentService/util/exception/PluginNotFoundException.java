package com.danikvitek.CommentService.util.exception;

public final class PluginNotFoundException extends RuntimeException {
    public PluginNotFoundException() {
        super("Plugin not found");
    }
}
