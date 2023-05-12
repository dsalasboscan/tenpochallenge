package com.davidsalas.tenpochallenge.adapter.out.jpa

import com.davidsalas.tenpochallenge.adapter.out.jpa.model.CalculationJpaModel
import com.davidsalas.tenpochallenge.application.port.out.CalculationRepository
import com.davidsalas.tenpochallenge.domain.Calculation
import com.davidsalas.tenpochallenge.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.Instant

class CalculationJpaAdapterTest extends Specification {
    CalculationJpaRepository calculationJpaRepository = Mock()
    CalculationRepository target = new CalculationJpaAdapter(calculationJpaRepository)

    def "given a cashout to save on the database when the insert is ok then return the cashout"() {
        given:
        def currentDate = Instant.now()

        def id = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")

        def calculation = Calculation.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        def calculationEntity = CalculationJpaModel.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        when:
        target.save(calculation)

        then:
        1 * calculationJpaRepository.save(calculationEntity) >> calculationEntity
    }

    def "given a call to findLast then return the last calculation"() {
        given:
        def currentDate = Instant.now()

        def id = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")

        def calculationEntity = CalculationJpaModel.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        def calculation = Calculation.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        when:
        def response = target.findLast()

        then:
        1 * calculationJpaRepository.findLast() >> Optional.of(calculationEntity)
        response == Optional.of(calculation)
    }

    def "given a call to findLast when any calculation exist return optional empty"() {
        given:

        when:
        def response = target.findLast()

        then:
        1 * calculationJpaRepository.findLast() >> Optional.empty()
        response == Optional.empty()
    }

    def "given a call to findAll by page and size then return calculations"() {
        given:
        def currentDate = Instant.now()

        def id = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")

        def calculationEntity1 = CalculationJpaModel.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        def calculationEntity2 = CalculationJpaModel.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        def calculationEntities = List.of(calculationEntity1, calculationEntity2)

        def calculation1 = Calculation.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        def calculation2 = Calculation.builder()
                .id(id)
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(currentDate)
                .build()

        def calculations = List.of(calculation1, calculation2)

        def pageable = PageRequest.of(0, 10)

        def page = new PageImpl<>(calculationEntities, pageable, 20)

        def expected = Page.<Calculation>builder()
                .content(calculations)
                .size(10)
                .number(0)
                .totalElements(20)
                .totalPages(2)
                .build()

        when:
        def response = target.findAll(0, 10)

        then:
        calculationJpaRepository.findAll(pageable) >> page
        response == expected
    }
}
