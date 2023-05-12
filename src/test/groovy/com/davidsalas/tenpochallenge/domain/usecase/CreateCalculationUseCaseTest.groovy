package com.davidsalas.tenpochallenge.domain.usecase

import com.davidsalas.tenpochallenge.application.exception.EntityNotFoundException
import com.davidsalas.tenpochallenge.application.port.in.CreateCalculationCommand
import com.davidsalas.tenpochallenge.application.port.out.CalculationRepository
import com.davidsalas.tenpochallenge.application.port.out.PercentageRepository
import com.davidsalas.tenpochallenge.domain.Calculation
import com.davidsalas.tenpochallenge.domain.Percentage
import spock.lang.Specification

import java.time.Clock
import java.time.Instant

class CreateCalculationUseCaseTest extends Specification {

    PercentageRepository percentageRepository = Mock()
    CalculationRepository calculationRepository = Mock()
    Clock clock = Mock()

    CreateCalculationCommand target = new CreateCalculationUseCase(percentageRepository, calculationRepository, clock)

    def "given two numbers to be added, when the external api provides the fee percentage then do the calculation, store on the database an return response"() {
        given:
        def calculationId = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")
        def targetSpy = Spy(target)
        targetSpy.generateCalculationId() >> calculationId

        def currentDate = Instant.now()
        clock.instant() >> currentDate

        def data = CreateCalculationCommand.Data.builder()
                .firstValue(5.00)
                .secondValue(5.00)
                .build()

        def percentage = Percentage.builder().value(0.1).build()

        def calculation = Calculation.builder()
                .id(calculationId)
                .firstValue(5.00)
                .secondValue(5.00)
                .percentage(0.1)
                .calculatedValue(11.00)
                .createdAt(currentDate)
                .build()

        when:
        def response = targetSpy.execute(data)

        then:
        1 * percentageRepository.findPercentage() >> percentage
        1 * calculationRepository.save(calculation)
        response == calculation
    }

    def "given two numbers to be added, when the external fails and a previous calculation exist return a new calculation with the previous fee percentage"() {
        given:
        def calculationId = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")
        def targetSpy = Spy(target)
        targetSpy.generateCalculationId() >> calculationId

        def currentDate = Instant.now()
        clock.instant() >> currentDate

        def data = CreateCalculationCommand.Data.builder()
                .firstValue(10.00)
                .secondValue(10.00)
                .build()

        def previousCalculation = Calculation.builder()
                .id(calculationId)
                .firstValue(5.00)
                .secondValue(5.00)
                .percentage(0.1)
                .calculatedValue(11.00)
                .createdAt(currentDate)
                .build()

        def calculation = Calculation.builder()
                .id(calculationId)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        when:
        def response = targetSpy.execute(data)

        then:
        1 * percentageRepository.findPercentage() >> { throw new Exception() }
        1 * calculationRepository.findLast() >> Optional.of(previousCalculation)
        1 * calculationRepository.save(calculation)
        response == calculation
    }

    def "given two numbers to be added, when the external fails and a previous calculation dont exist then throw error"() {
        given:
        def calculationId = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")
        def targetSpy = Spy(target)
        targetSpy.generateCalculationId() >> calculationId

        def currentDate = Instant.now()
        clock.instant() >> currentDate

        def data = CreateCalculationCommand.Data.builder()
                .firstValue(5.00)
                .secondValue(5.00)
                .build()

        when:
        targetSpy.execute(data)

        then:
        1 * percentageRepository.findPercentage() >> { throw new Exception() }
        1 * calculationRepository.findLast() >> Optional.empty()
        0 * calculationRepository.save(_)
        thrown(EntityNotFoundException)
    }
}
