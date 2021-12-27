package com.danikvitek.PluginService.util.exception;

public final class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Category not found");
    }
}
