package com.davidsalas.tenpochallenge.domain.usecase

import com.davidsalas.tenpochallenge.application.port.in.GetCalculationsQuery
import com.davidsalas.tenpochallenge.application.port.out.CalculationRepository
import com.davidsalas.tenpochallenge.domain.Calculation
import com.davidsalas.tenpochallenge.domain.Page
import spock.lang.Specification

import java.time.Instant

class GetCalculationsUseCaseTest extends Specification {
    CalculationRepository calculationJpaAdapter = Mock()
    GetCalculationsQuery target = new GetCalculationsUseCase(calculationJpaAdapter, 0, 10)

    def "given page and size when the find all method of CalculationRepository returns ok then return the calculations"() {
        given:
        def currentDate = Instant.now()

        def id = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")

        def data = GetCalculationsQuery.Data.builder().page(page).size(size).build()

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

        def expected = Page.<Calculation>builder()
                .content(calculations)
                .size(expected_size)
                .number(expected_page)
                .totalElements(20000)
                .build()

        when:
        def response = target.execute(data)

        then:
        1 * calculationJpaAdapter.findAll(expected_page, expected_size) >> expected
        response == expected

        where:
        page | size | expected_page | expected_size
        null | 10   | 0             | 10
        10   | null | 10            | 10
        null | null | 0             | 10
        20   | 20   | 20            | 20
    }

}
