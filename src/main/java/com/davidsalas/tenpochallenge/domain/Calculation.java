package com.davidsalas.tenpochallenge.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
@Generated
@Slf4j
public class Calculation {
    UUID id;
    BigDecimal firstValue;
    BigDecimal secondValue;
    BigDecimal percentage;
    BigDecimal calculatedValue;
    Instant createdAt;

    public Calculation(UUID id, BigDecimal firstValue, BigDecimal secondValue, BigDecimal percentage, Instant creationDate) {
        this.id = id;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.percentage = percentage;
        this.calculatedValue = calculateValue();
        this.createdAt = creationDate;
    }

    private BigDecimal calculateValue() {
        BigDecimal valuesAdded = firstValue.add(secondValue);
        BigDecimal calculatedValue = valuesAdded.add(valuesAdded.multiply(percentage));
        log.info("Calculated values: ({} + {}) * {} = {}", firstValue, secondValue, percentage, calculatedValue);
        return calculatedValue.setScale(2, RoundingMode.CEILING);
    }
}
