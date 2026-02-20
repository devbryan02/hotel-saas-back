package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class ClientNotFoundException extends BusinessException {

    public ClientNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
