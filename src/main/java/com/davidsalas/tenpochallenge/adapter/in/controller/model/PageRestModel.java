package com.davidsalas.tenpochallenge.adapter.in.controller.model;

import com.davidsalas.tenpochallenge.domain.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageRestModel<T> {
    List<T> content;
    long totalElements;
    int size;
    int number;
    int totalPages;

    public <U> PageRestModel<T> fromDomain(Page<U> domain, Function<U, T> convert) {
        return PageRestModel.<T>builder()
            .content(domain.getContent()
                .stream()
                .map(convert)
                .collect(Collectors.toList()))
            .totalElements(domain.getTotalElements())
            .size(domain.getSize())
            .number(domain.getNumber())
            .totalPages(domain.getTotalPages())
            .build();
    }
}
