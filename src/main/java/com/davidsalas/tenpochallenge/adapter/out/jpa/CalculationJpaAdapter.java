package com.davidsalas.tenpochallenge.adapter.out.jpa;

import com.davidsalas.tenpochallenge.adapter.out.jpa.model.CalculationJpaModel;
import com.davidsalas.tenpochallenge.application.port.out.CalculationRepository;
import com.davidsalas.tenpochallenge.domain.Calculation;
import com.davidsalas.tenpochallenge.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CalculationJpaAdapter implements CalculationRepository {

    private final CalculationJpaRepository calculationJpaRepository;

    public CalculationJpaAdapter(CalculationJpaRepository calculationJpaRepository) {
        this.calculationJpaRepository = calculationJpaRepository;
    }

    @Override
    @Async
    public void save(Calculation calculation) {
        CalculationJpaModel calculationJpaModel = CalculationJpaModel.fromDomain(calculation);
        calculationJpaRepository.save(calculationJpaModel).toDomain();
    }

    @Override
    public Optional<Calculation> findLast() {
        Optional<CalculationJpaModel> calculation = calculationJpaRepository.findLast();

        return calculation.map(CalculationJpaModel::toDomain);
    }

    @Override
    public Page<Calculation> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<CalculationJpaModel> calculations =  calculationJpaRepository.findAll(pageable);
        return Page.<Calculation>builder()
                .content(calculations.stream().map(CalculationJpaModel::toDomain).collect(Collectors.toList()))
                .size(size)
                .number(page)
                .totalElements(calculations.getTotalElements())
                .totalPages(calculations.getTotalPages())
                .build();
    }
}
