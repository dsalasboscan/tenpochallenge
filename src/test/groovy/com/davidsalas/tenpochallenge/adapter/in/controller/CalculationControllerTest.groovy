package com.davidsalas.tenpochallenge.adapter.in.controller

import com.davidsalas.tenpochallenge.adapter.in.controller.handler.ErrorHandler
import com.davidsalas.tenpochallenge.adapter.in.controller.model.CreateCalculationRequestBody
import com.davidsalas.tenpochallenge.application.port.in.CreateCalculationCommand
import com.davidsalas.tenpochallenge.application.port.in.GetCalculationsQuery
import com.davidsalas.tenpochallenge.domain.Calculation
import com.davidsalas.tenpochallenge.domain.Page
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.Instant

import static org.hamcrest.core.Is.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class CalculationControllerTest extends Specification {

    CreateCalculationCommand createCalculationCommand = Mock()
    GetCalculationsQuery getCalculationsQuery = Mock()

    ObjectMapper mapper = new ObjectMapper()

    CalculationController target = new CalculationController(
            createCalculationCommand,
            getCalculationsQuery
    )

    MockMvc mvc = standaloneSetup(target).setControllerAdvice(new ErrorHandler()).build()

    def "given a request  POST performed to /calculations with tu numbers when the process is ok then return the values calculated"() {
        given:
        def request = CreateCalculationRequestBody.builder().firstValue(5.00).secondValue(5.00).build()

        def data = CreateCalculationCommand.Data.builder()
                .firstValue(5.00)
                .secondValue(5.00)
                .build()

        def calculation = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311"))
                .firstValue(5.00)
                .secondValue(5.00)
                .percentage(0.1)
                .calculatedValue(11.00)
                .createdAt(Instant.now())
                .build()

        when:
        def result = mvc.perform(post("/calculations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath('$.id', is("b4cd91be-e5e7-4549-b877-c427c8900311")))
                .andExpect(jsonPath('$.first_value', is(5.00.doubleValue())))
                .andExpect(jsonPath('$.second_value', is(5.00.doubleValue())))
                .andExpect(jsonPath('$.percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.calculated_value', is(11.00.doubleValue())))
                .andReturn()

        then:
        1 * createCalculationCommand.execute(data) >> calculation
        result.response.status == 201
    }

    def "given a request GET performed to /calculations without page and size params then return the data with default pagination"() {
        given:
        def data = GetCalculationsQuery.Data.builder().build()

        def calculation1 = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311"))
                .firstValue(5.00)
                .secondValue(5.00)
                .percentage(0.1)
                .calculatedValue(11.00)
                .createdAt(Instant.now())
                .build()

        def calculation2 = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900312"))
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(Instant.now())
                .build()

        def calculation3 = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900313"))
                .firstValue(30.00)
                .secondValue(30.00)
                .percentage(0.1)
                .calculatedValue(66.00)
                .createdAt(Instant.now())
                .build()

        def calculations = Page.builder()
                .content(List.of(calculation1, calculation2, calculation3))
                .totalPages(2)
                .number(0)
                .size(10)
                .build()

        when:
        def result = mvc.perform(get("/calculations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.content.[0].id', is("b4cd91be-e5e7-4549-b877-c427c8900311")))
                .andExpect(jsonPath('$.content.[0].first_value', is(5.00.doubleValue())))
                .andExpect(jsonPath('$.content.[0].second_value', is(5.00.doubleValue())))
                .andExpect(jsonPath('$.content.[0].percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.content.[0].calculated_value', is(11.00.doubleValue())))
                .andExpect(jsonPath('$.content.[1].id', is("b4cd91be-e5e7-4549-b877-c427c8900312")))
                .andExpect(jsonPath('$.content.[1].first_value', is(10.00.doubleValue())))
                .andExpect(jsonPath('$.content.[1].second_value', is(10.00.doubleValue())))
                .andExpect(jsonPath('$.content.[1].percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.content.[1].calculated_value', is(22.00.doubleValue())))
                .andExpect(jsonPath('$.content.[2].id', is("b4cd91be-e5e7-4549-b877-c427c8900313")))
                .andExpect(jsonPath('$.content.[2].first_value', is(30.00.doubleValue())))
                .andExpect(jsonPath('$.content.[2].second_value', is(30.00.doubleValue())))
                .andExpect(jsonPath('$.content.[2].percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.content.[2].calculated_value', is(66.00.doubleValue())))
                .andExpect(jsonPath('$.number', is(0)))
                .andExpect(jsonPath('$.total_pages', is(2)))
                .andExpect(jsonPath('$.size', is(10)))
                .andReturn()

        then:
        1 * getCalculationsQuery.execute(data) >> calculations
        result.response.status == 200
    }

    def "given a request GET performed to /calculations with page and size params then return the data with pagination"() {
        given:
        def data = GetCalculationsQuery.Data.builder().page(0).size(15).build()

        def calculation1 = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900311"))
                .firstValue(5.00)
                .secondValue(5.00)
                .percentage(0.1)
                .calculatedValue(11.00)
                .createdAt(Instant.now())
                .build()

        def calculation2 = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900312"))
                .firstValue(10.00)
                .secondValue(10.00)
                .percentage(0.1)
                .calculatedValue(22.00)
                .createdAt(Instant.now())
                .build()

        def calculation3 = Calculation.builder()
                .id(UUID.fromString("b4cd91be-e5e7-4549-b877-c427c8900313"))
                .firstValue(30.00)
                .secondValue(30.00)
                .percentage(0.1)
                .calculatedValue(66.00)
                .createdAt(Instant.now())
                .build()

        def calculations = Page.builder()
                .content(List.of(calculation1, calculation2, calculation3))
                .totalPages(2)
                .number(0)
                .size(10)
                .build()

        when:
        def result = mvc.perform(get("/calculations?page=0&size=15")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.content.[0].id', is("b4cd91be-e5e7-4549-b877-c427c8900311")))
                .andExpect(jsonPath('$.content.[0].first_value', is(5.00.doubleValue())))
                .andExpect(jsonPath('$.content.[0].second_value', is(5.00.doubleValue())))
                .andExpect(jsonPath('$.content.[0].percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.content.[0].calculated_value', is(11.00.doubleValue())))
                .andExpect(jsonPath('$.content.[1].id', is("b4cd91be-e5e7-4549-b877-c427c8900312")))
                .andExpect(jsonPath('$.content.[1].first_value', is(10.00.doubleValue())))
                .andExpect(jsonPath('$.content.[1].second_value', is(10.00.doubleValue())))
                .andExpect(jsonPath('$.content.[1].percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.content.[1].calculated_value', is(22.00.doubleValue())))
                .andExpect(jsonPath('$.content.[2].id', is("b4cd91be-e5e7-4549-b877-c427c8900313")))
                .andExpect(jsonPath('$.content.[2].first_value', is(30.00.doubleValue())))
                .andExpect(jsonPath('$.content.[2].second_value', is(30.00.doubleValue())))
                .andExpect(jsonPath('$.content.[2].percentage', is(0.1.doubleValue())))
                .andExpect(jsonPath('$.content.[2].calculated_value', is(66.00.doubleValue())))
                .andExpect(jsonPath('$.number', is(0)))
                .andExpect(jsonPath('$.total_pages', is(2)))
                .andExpect(jsonPath('$.size', is(10)))
                .andReturn()

        then:
        1 * getCalculationsQuery.execute(data) >> calculations
        result.response.status == 200
    }
}
