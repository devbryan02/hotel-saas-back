package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidOccupationDatesException extends BusinessException {

    public InvalidOccupationDatesException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
