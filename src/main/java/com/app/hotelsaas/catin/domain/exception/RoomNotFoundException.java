package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends BusinessException {

    public RoomNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
