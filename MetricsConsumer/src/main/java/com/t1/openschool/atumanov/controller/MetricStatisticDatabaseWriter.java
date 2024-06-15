package com.t1.openschool.atumanov.controller;

import com.t1.openschool.atumanov.model.MetricStatistic;
import com.t1.openschool.atumanov.service.MetricStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetricStatisticDatabaseWriter implements MetricStatisticProcessor{

    private final MetricStatisticService service;

    @Override
    public void process(MetricStatistic statistic) {
        statistic.setNotCreatedInDb(service.findById(statistic.getId()).block() == null);
        service.save(statistic).subscribe(unused -> { },
                                            throwable -> {
            log.error("Could not write to database, error: {}", throwable.getMessage());
        });
    }
}
