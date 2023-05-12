package com.davidsalas.tenpochallenge.domain.usecase;

import com.davidsalas.tenpochallenge.application.config.ErrorCode;
import com.davidsalas.tenpochallenge.application.exception.EntityNotFoundException;
import com.davidsalas.tenpochallenge.application.port.in.CreateCalculationCommand;
import com.davidsalas.tenpochallenge.application.port.out.CalculationRepository;
import com.davidsalas.tenpochallenge.application.port.out.PercentageRepository;
import com.davidsalas.tenpochallenge.domain.Calculation;
import com.davidsalas.tenpochallenge.domain.Percentage;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

@Component
public class CreateCalculationUseCase implements CreateCalculationCommand {

    private final PercentageRepository percentageRestAdapter;
    private final CalculationRepository calculationJpaAdapter;
    private final Clock clock;

    public CreateCalculationUseCase(PercentageRepository percentageRestAdapter,
                                    CalculationRepository calculationJpaAdapter,
                                    Clock clock) {
        this.percentageRestAdapter = percentageRestAdapter;
        this.calculationJpaAdapter = calculationJpaAdapter;
        this.clock = clock;
    }

    @Override
    public Calculation execute(Data data) {
        Percentage percentage;

        try {
            percentage = percentageRestAdapter.findPercentage();
        } catch (Exception e) {
            percentage =  Percentage.builder().value(getLastCalculation().getPercentage()).build();
        }

        Calculation calculation = new Calculation(
                generateCalculationId(), data.getFirstValue(), data.getSecondValue(), percentage.getValue(), clock.instant()
        );

        calculationJpaAdapter.save(calculation);

        return calculation;
    }

    public UUID generateCalculationId() {
        return UUID.randomUUID();
    }

    private Calculation getLastCalculation() {
        Optional<Calculation> lastCalculation = calculationJpaAdapter.findLast();

        if (lastCalculation.isEmpty())
            throw new EntityNotFoundException(ErrorCode.LAST_CALCULATION_NOT_FOUND);

        return lastCalculation.get();
    }
}
