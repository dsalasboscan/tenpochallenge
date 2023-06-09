package com.davidsalas.tenpochallenge.adapter.out.rest.handler;

import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import com.davidsalas.tenpochallenge.application.exception.RepositoryNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    private final Map<HttpStatus, RuntimeException> exceptionsMap;

    public RestTemplateErrorHandler(Map<HttpStatus, RuntimeException> exceptionsMap) {
        this.exceptionsMap = exceptionsMap;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error(
                Optional.of(response.getBody())
                        .map(RestTemplateErrorHandler::readBody)
                        .orElse(response.getBody().toString())
        );
        throw exceptionsMap.getOrDefault(response.getStatusCode(),
                new RepositoryNotAvailableException(ErrorCode.REPOSITORY_NOT_AVAILABLE));
    }

    private static String readBody(InputStream body) {
        try {
            return new String(body.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
