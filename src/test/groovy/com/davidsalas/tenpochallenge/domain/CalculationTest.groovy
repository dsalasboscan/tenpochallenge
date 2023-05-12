package com.davidsalas.tenpochallenge.domain

import spock.lang.Specification

import java.time.Instant

class CalculationTest extends Specification {

    def "given different variables then calculate the correct values"() {
        given:
        def currentDate = Instant.now()
        def id = UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311")

        when:
        def calculation = new Calculation(id, first_value, second_value, percentage, currentDate)

        then:
        calculation.calculatedValue == expected_calculated_value

        where:
        first_value | second_value | percentage | expected_calculated_value
        5.00        | 5.00         | 0.1        | 11
        5.00        | 5.00         | 0.2        | 12
    }
}
