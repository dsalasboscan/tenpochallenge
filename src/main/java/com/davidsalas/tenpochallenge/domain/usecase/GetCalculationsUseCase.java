package com.davidsalas.tenpochallenge.domain.usecase;

import com.davidsalas.tenpochallenge.application.port.in.GetCalculationsQuery;
import com.davidsalas.tenpochallenge.application.port.out.CalculationRepository;
import com.davidsalas.tenpochallenge.domain.Calculation;
import com.davidsalas.tenpochallenge.domain.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetCalculationsUseCase implements GetCalculationsQuery {

    private final CalculationRepository calculationJpaAdapter;

    private final Integer defaultPage;
    private final Integer defaultSize;

    public GetCalculationsUseCase(CalculationRepository calculationJpaAdapter,
                                  @Value("${application.pagination.default-page}") Integer defaultPage,
                                  @Value("${application.pagination.default-size}") Integer defaultSize) {
        this.calculationJpaAdapter = calculationJpaAdapter;
        this.defaultPage = defaultPage;
        this.defaultSize = defaultSize;
    }

    @Override
    public Page<Calculation> execute(Data data) {
        Integer page = data.getPage() != null ? data.getPage() : defaultPage;
        Integer size = data.getSize() != null ? data.getSize() : defaultSize;

        return calculationJpaAdapter.findAll(page, size);
    }
}
