package com.davidsalas.tenpochallenge.application.port.out;

import com.davidsalas.tenpochallenge.domain.Calculation;
import com.davidsalas.tenpochallenge.domain.Page;

import java.util.Optional;

public interface CalculationRepository {
    void save(Calculation calculation);
    Optional<Calculation> findLast();
    Page<Calculation> findAll(Integer page, Integer size);
}
