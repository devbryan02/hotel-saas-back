package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class RoomNotAvailableException extends BusinessException {

    public RoomNotAvailableException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
