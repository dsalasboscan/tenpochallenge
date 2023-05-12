package com.davidsalas.tenpochallenge.application.port.in;

import com.davidsalas.tenpochallenge.domain.Calculation;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

public interface CreateCalculationCommand {
    Calculation execute(Data data);

    @Builder
    @Value
    class Data {
        BigDecimal firstValue;
        BigDecimal secondValue;
    }
}
