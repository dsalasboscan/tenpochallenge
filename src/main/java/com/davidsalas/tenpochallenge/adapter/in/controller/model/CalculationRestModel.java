package com.davidsalas.tenpochallenge.adapter.in.controller.model;

import com.davidsalas.tenpochallenge.domain.Calculation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CalculationRestModel {
    String id;
    BigDecimal firstValue;
    BigDecimal secondValue;
    BigDecimal percentage;
    BigDecimal calculatedValue;
    Instant createdAt;

    public static CalculationRestModel fromDomain(Calculation calculation) {
        return CalculationRestModel.builder()
                .id(calculation.getId().toString())
                .firstValue(calculation.getFirstValue())
                .secondValue(calculation.getSecondValue())
                .percentage(calculation.getPercentage())
                .calculatedValue(calculation.getCalculatedValue())
                .createdAt(calculation.getCreatedAt())
                .build();
    }
}
