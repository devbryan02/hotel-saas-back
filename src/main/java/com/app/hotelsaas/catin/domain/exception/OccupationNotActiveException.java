package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class OccupationNotActiveException extends BusinessException {

    public OccupationNotActiveException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
