package com.davidsalas.tenpochallenge.application.port.in;

import com.davidsalas.tenpochallenge.domain.Calculation;
import com.davidsalas.tenpochallenge.domain.Page;
import lombok.Builder;
import lombok.Value;

public interface GetCalculationsQuery {
    Page<Calculation> execute(Data data);

    @Builder
    @Value
    class Data {
        Integer page;
        Integer size;
    }
}
