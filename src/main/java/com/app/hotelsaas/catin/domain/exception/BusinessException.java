package com.app.hotelsaas.catin.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    protected BusinessException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
