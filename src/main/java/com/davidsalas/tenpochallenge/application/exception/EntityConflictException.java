package com.davidsalas.tenpochallenge.application.exception;

import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import org.springframework.http.HttpStatus;

public class EntityConflictException extends HttpErrorException {
    public EntityConflictException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST.value(), errorCode);
    }
}
