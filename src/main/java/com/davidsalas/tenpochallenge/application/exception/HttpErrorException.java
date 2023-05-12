package com.davidsalas.tenpochallenge.application.exception;


import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import com.davidsalas.tenpochallenge.application.exception.model.ErrorResponse;

import java.util.Collections;
import java.util.Map;

public abstract class HttpErrorException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public HttpErrorException(int httpStatus, ErrorCode errorCode, Map<String, String> fields) {
        this.errorResponse = ErrorResponse.builder()
                .fields(fields)
                .code(errorCode)
                .httpStatus(httpStatus)
                .build();
    }

    public HttpErrorException(int httpStatus, ErrorCode errorCode) {
        this.errorResponse = ErrorResponse.builder()
                .fields(Collections.emptyMap())
                .code(errorCode)
                .httpStatus(httpStatus)
                .build();
    }

    public ErrorResponse getErrorResponse() {
        return this.errorResponse;
    }

}
