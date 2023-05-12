package com.davidsalas.tenpochallenge.adapter.out.rest;

import com.davidsalas.tenpochallenge.adapter.out.rest.handler.RestTemplateErrorHandler;
import com.davidsalas.tenpochallenge.adapter.out.rest.model.PercentageResponse;
import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import com.davidsalas.tenpochallenge.application.exception.EntityBadRequestException;
import com.davidsalas.tenpochallenge.application.exception.EntityConflictException;
import com.davidsalas.tenpochallenge.application.exception.EntityNotFoundException;
import com.davidsalas.tenpochallenge.application.exception.RepositoryNotAvailableException;
import com.davidsalas.tenpochallenge.application.port.out.PercentageRepository;
import com.davidsalas.tenpochallenge.domain.Percentage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public class PercentageRestAdapter implements PercentageRepository {

    private final RestTemplate restTemplate;
    private final String pathGetPercentages;
    private final RetryTemplate retryTemplate;

    public PercentageRestAdapter(RestTemplate restTemplate,
                                 RetryTemplate retryTemplate,
                                 @Value("${application.rest-client.percentage.url}") String pathGetPercentages) {
        this.restTemplate = restTemplate;
        this.pathGetPercentages = pathGetPercentages;
        this.retryTemplate = retryTemplate;

        var errorHandler = new RestTemplateErrorHandler(
                Map.of(
                        HttpStatus.BAD_REQUEST, new EntityBadRequestException(ErrorCode.PERCENTAGE_ENTITY_BAD_REQUEST),
                        HttpStatus.NOT_FOUND, new EntityNotFoundException(ErrorCode.PERCENTAGE_ENTITY_NOT_FOUND),
                        HttpStatus.CONFLICT, new EntityConflictException(ErrorCode.PERCENTAGE_REPOSITORY_CONFLICT),
                        HttpStatus.INTERNAL_SERVER_ERROR, new RepositoryNotAvailableException(ErrorCode.REPOSITORY_NOT_AVAILABLE),
                        HttpStatus.SERVICE_UNAVAILABLE, new RepositoryNotAvailableException(ErrorCode.REPOSITORY_NOT_AVAILABLE)
                )
        );

        this.restTemplate.setErrorHandler(errorHandler);
    }

    @Override
    @Cacheable(value = "percentage", key = "#root.method.name", cacheManager = "cacheManager")
    public Percentage findPercentage() {
        String uri = UriComponentsBuilder.fromHttpUrl(pathGetPercentages).toUriString();

        log.info("Doing request to uri GET {}", uri);

        PercentageResponse percentage = retryTemplate.execute(
                retryContext -> restTemplate.getForObject(uri, PercentageResponse.class)
        );

        if (percentage == null)
            throw new EntityNotFoundException(ErrorCode.PERCENTAGE_ENTITY_NOT_FOUND);

        log.info("Percentage response: {}", percentage);

        return percentage.toDomain();
    }
}
