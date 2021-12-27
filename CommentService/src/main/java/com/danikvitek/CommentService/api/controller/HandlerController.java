package com.danikvitek.CommentService.api.controller;

import com.danikvitek.CommentService.util.exception.AuthException;
import com.danikvitek.CommentService.util.exception.CommentNotFoundException;
import com.danikvitek.CommentService.util.exception.PluginNotFoundException;
import com.danikvitek.CommentService.util.exception.UserNotFoundException;
import com.danikvitek.CommentService.api.dto.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public final class HandlerController {
    private final MessageSource messageSource;

    @ExceptionHandler({
            UserNotFoundException.class,
            CommentNotFoundException.class,
            PluginNotFoundException.class
    })
    public @NotNull ResponseEntity<ErrorDto> handleNotFoundException(@NotNull RuntimeException e) {
        ErrorDto error = ErrorDto.builder()
                .code("404 NOT FOUND")
                .description(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            NumberFormatException.class
    })
    public @NotNull ResponseEntity<ErrorDto> handleBadRequestException(@NotNull RuntimeException e) {
        ErrorDto error = ErrorDto.builder()
                .code("400 BAD REQUEST")
                .description(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AuthException.class)
    public @NotNull ResponseEntity<ErrorDto> handleAuthException(@NotNull RuntimeException e) {
        ErrorDto error = ErrorDto.builder()
                .code("401 UNAUTHORIZED")
                .description(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public @NotNull ResponseEntity<Set<ErrorDto>> handleValidationException(@NotNull ConstraintViolationException e,
                                                                            WebRequest request) {
        Set<ErrorDto> errors = e.getConstraintViolations().stream()
                .map(violation -> ErrorDto.builder()
                        .code("400 BAD REQUEST")
                        .description(violation.getPropertyPath() + " invalid. " +
                                messageSource.getMessage(violation.getMessage(), null, request.getLocale()))
                        .build())
                .collect(Collectors.toSet());
        return ResponseEntity.badRequest().body(errors);
    }
}
