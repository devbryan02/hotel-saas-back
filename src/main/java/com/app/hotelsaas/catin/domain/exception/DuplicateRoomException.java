package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRoomException extends BusinessException {

    public DuplicateRoomException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
