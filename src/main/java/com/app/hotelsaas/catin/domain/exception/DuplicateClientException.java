package com.app.hotelsaas.catin.domain.exception;

import org.springframework.http.HttpStatus;

public class DuplicateClientException extends BusinessException {

    public DuplicateClientException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
