package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class TenantNotFoundException extends BusinessException {

    public TenantNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
