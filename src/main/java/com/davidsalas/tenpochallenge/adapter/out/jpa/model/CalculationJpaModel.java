package com.davidsalas.tenpochallenge.adapter.out.jpa.model;


import com.davidsalas.tenpochallenge.domain.Calculation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calculations")
public class CalculationJpaModel {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_value", nullable = false)
    private BigDecimal firstValue;

    @Column(name = "second_value", nullable = false)
    private BigDecimal secondValue;

    @Column(name = "percentage", nullable = false)
    private BigDecimal percentage;

    @Column(name = "calculated_value", nullable = false)
    private BigDecimal calculatedValue;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public static CalculationJpaModel fromDomain(Calculation calculation) {
        return CalculationJpaModel.builder()
                .id(calculation.getId())
                .firstValue(calculation.getFirstValue())
                .secondValue(calculation.getSecondValue())
                .percentage(calculation.getPercentage())
                .calculatedValue(calculation.getCalculatedValue())
                .createdAt(calculation.getCreatedAt())
                .build();
    }

    public Calculation toDomain() {
        return Calculation.builder()
                .id(id)
                .firstValue(firstValue)
                .secondValue(secondValue)
                .percentage(percentage)
                .calculatedValue(calculatedValue)
                .createdAt(createdAt)
                .build();
    }
}