package com.davidsalas.tenpochallenge.adapter.out.rest

import com.davidsalas.tenpochallenge.adapter.out.rest.model.PercentageResponse
import com.davidsalas.tenpochallenge.application.exception.EntityNotFoundException
import com.davidsalas.tenpochallenge.application.port.out.PercentageRepository
import com.davidsalas.tenpochallenge.domain.Percentage
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class PercentageRestAdapterTest extends Specification {

    RestTemplate restTemplate = Mock()
    RetryTemplate retryTemplate
    PercentageRepository target

    def setup() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy()
        retryPolicy.setMaxAttempts(4)

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy()
        backOffPolicy.setBackOffPeriod(1500)

        retryTemplate = new RetryTemplate()
        retryTemplate.setRetryPolicy(retryPolicy)
        retryTemplate.setBackOffPolicy(backOffPolicy)

        target =  new PercentageRestAdapter(restTemplate, retryTemplate, "https://percentages.free.beeceptor.com/percentages")
    }

    def "given a call to findPercentages when the external api returns a response then map to domain and return it"() {
        given:
        def percentageResponse = PercentageResponse.builder().value(0.1).build()
        def percentage = Percentage.builder().value(0.1).build()

        when:
        def response = target.findPercentage()

        then:
        1 * restTemplate.getForObject("https://percentages.free.beeceptor.com/percentages", PercentageResponse.class) >> percentageResponse
        response == percentage
    }

    def "given a call to findPercentages when the external api returns an error try three times and then throw error after exhaust the retries"() {
        given:

        when:
        target.findPercentage()

        then:
        4 * restTemplate.getForObject("https://percentages.free.beeceptor.com/percentages", PercentageResponse.class) >> { throw new Exception() }
        thrown(Exception)
    }

    def "given a call to findPercentages when the external api returns null then map to domain and return it"() {
        given:

        when:
        target.findPercentage()

        then:
        1 * restTemplate.getForObject("https://percentages.free.beeceptor.com/percentages", PercentageResponse.class)
        thrown(EntityNotFoundException)
    }
}
