package com.davidsalas.tenpochallenge.adapter.in.controller.handler;

import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import com.davidsalas.tenpochallenge.application.exception.HttpErrorException;
import com.davidsalas.tenpochallenge.application.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandler {


    @ExceptionHandler({HttpErrorException.class})
    public ResponseEntity<ErrorResponse> handle(HttpErrorException ex) {
        log.error("Error handled : ", ex);
        return ResponseEntity.status(ex.getErrorResponse().getHttpStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException ex) {
        log.error("Bad argument", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().code(ErrorCode.METHOD_ARGUMENT_MISMATCH).build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        log.error("Bad argument", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(camelToSnake(fieldName), errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().code(ErrorCode.METHOD_ARGUMENT_NOT_VALID).fields(errors).build());
    }


    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ErrorResponse> handle(Throwable ex) {
        log.error("Unexpected Error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().code(ErrorCode.UNEXPECTED_ERROR).build());
    }

    private static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
