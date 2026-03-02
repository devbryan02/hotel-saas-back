package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class OccupationNotFoundException extends BusinessException {

    public OccupationNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
