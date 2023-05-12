package com.davidsalas.tenpochallenge.adapter.out.rest.model;

import com.davidsalas.tenpochallenge.domain.Percentage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PercentageResponse {
    BigDecimal value;

    public Percentage toDomain() {
        return Percentage.builder().value(value).build();
    }
}
