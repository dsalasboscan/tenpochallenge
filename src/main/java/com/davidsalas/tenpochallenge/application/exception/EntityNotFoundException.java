package com.davidsalas.tenpochallenge.application.exception;

import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends HttpErrorException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND.value(), errorCode);
    }
}
