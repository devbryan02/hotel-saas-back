package com.app.hotelsaas.catin.web.rest.exception;

import com.app.hotelsaas.catin.domain.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ){
        // Builds API error response from exception details
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ex.getMessage(),
                ex.getHttpStatus().value(),
                ex.getClass().getSimpleName().replace("Exception", ""),
                request.getRequestURI()
        );

        log.error("Business exception occurred: {}", ex.getMessage(), ex);

        return ResponseEntity.status(ex.getHttpStatus()).body(apiErrorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ){
        // Maps validation errors to API error format
        List<ApiErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiErrorResponse.FieldError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .toList();

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                "Validation failed:",
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                request.getRequestURI(),
                fieldErrors));
    }

    /**
     * Handles missing parameter exceptions; returns error response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String message = ex.getParameterName() + " is required";

        ApiErrorResponse response = new ApiErrorResponse(
                message,
                HttpStatus.BAD_REQUEST.value(),
                "MissingRequiredParameter",
                request.getRequestURI()
        );

        log.warn("Missing parameter: {}", message, ex);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles and logs unexpected exceptions; returns generic error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                request.getRequestURI()
        );

        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
