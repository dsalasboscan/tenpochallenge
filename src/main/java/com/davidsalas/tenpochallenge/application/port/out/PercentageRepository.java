package com.davidsalas.tenpochallenge.application.port.out;

import com.davidsalas.tenpochallenge.domain.Percentage;

public interface PercentageRepository {
    Percentage findPercentage();
}
