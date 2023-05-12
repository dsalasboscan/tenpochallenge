package com.davidsalas.tenpochallenge.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class Page<T> {
    List<T> content;
    long totalElements;
    int size;
    int number;
    int totalPages;
}
