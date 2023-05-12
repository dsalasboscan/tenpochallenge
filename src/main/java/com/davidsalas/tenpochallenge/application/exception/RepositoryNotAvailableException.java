package com.davidsalas.tenpochallenge.application.exception;

import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import org.springframework.http.HttpStatus;

public class RepositoryNotAvailableException extends HttpErrorException {

    public RepositoryNotAvailableException(ErrorCode errorCode) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), errorCode);
    }
}
