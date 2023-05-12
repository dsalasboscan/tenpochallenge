package com.davidsalas.tenpochallenge.adapter.in.controller;

import com.davidsalas.tenpochallenge.adapter.in.controller.model.PageRestModel;
import com.davidsalas.tenpochallenge.application.port.in.CreateCalculationCommand;
import com.davidsalas.tenpochallenge.adapter.in.controller.model.CalculationRestModel;
import com.davidsalas.tenpochallenge.adapter.in.controller.model.CreateCalculationRequestBody;
import com.davidsalas.tenpochallenge.application.port.in.GetCalculationsQuery;
import com.davidsalas.tenpochallenge.domain.Calculation;
import com.davidsalas.tenpochallenge.domain.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CalculationController {

    private static final String PATH_CALCULATIONS = "/calculations";

    private final CreateCalculationCommand createCalculationCommand;
    private final GetCalculationsQuery getCalculationsQuery;

    public CalculationController(CreateCalculationCommand createCalculationCommand,
                                 GetCalculationsQuery getCalculationsQuery) {
        this.createCalculationCommand = createCalculationCommand;
        this.getCalculationsQuery = getCalculationsQuery;
    }

    @PostMapping(PATH_CALCULATIONS)
    @ResponseStatus(HttpStatus.CREATED)
    public CalculationRestModel createCalculation(
            @RequestBody CreateCalculationRequestBody requestBody
    ) {
        log.info("Received call to POST {}", PATH_CALCULATIONS);
        log.debug("With body {}", requestBody);
        CreateCalculationCommand.Data data = CreateCalculationCommand.Data.builder()
                .firstValue(requestBody.getFirstValue())
                .secondValue(requestBody.getSecondValue())
                .build();

        Calculation calculation =  createCalculationCommand.execute(data);
        log.info("Calculation created with response {}", calculation);
        return CalculationRestModel.fromDomain(calculation);
    }

    @GetMapping(PATH_CALCULATIONS)
    @ResponseStatus(HttpStatus.OK)
    public PageRestModel<CalculationRestModel> getCalculations(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) {
        log.info("Received call to GET {} with page: {} and size: {}", PATH_CALCULATIONS, page, size);

        GetCalculationsQuery.Data data = GetCalculationsQuery.Data.builder()
                .page(page)
                .size(size)
                .build();

        Page<Calculation> calculations =  getCalculationsQuery.execute(data);
        log.debug("Calculations fetched {}", calculations);
        return new PageRestModel<CalculationRestModel>().fromDomain(calculations, CalculationRestModel::fromDomain);
    }
}
