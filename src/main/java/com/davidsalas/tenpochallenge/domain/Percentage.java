package com.davidsalas.tenpochallenge.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Percentage {
    BigDecimal value;
}
