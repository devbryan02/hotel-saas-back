package com.app.hotelsaas.catin.web.rest.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(

        String message,
        int status,
        String error,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        String path,
        List<FieldError> fieldErrors
) {

    // Constructor simple para errores sin validaciones
    public ApiErrorResponse(String message, int status, String error, String path) {
        this(message, status, error, LocalDateTime.now(), path, null);
    }

    // Constructor para errores con validaciones
    public ApiErrorResponse(String message, int status, String error, String path, List<FieldError> fieldErrors) {
        this(message, status, error, LocalDateTime.now(), path, fieldErrors);
    }

    // Record interno para errores de campo
    public record FieldError(
            String field,
            String message,
            Object rejectedValue
    ) {}
}