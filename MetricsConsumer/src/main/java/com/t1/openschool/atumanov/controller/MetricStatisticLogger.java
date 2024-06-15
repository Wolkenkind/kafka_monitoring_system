package com.t1.openschool.atumanov.controller;

import com.t1.openschool.atumanov.model.MetricStatistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MetricStatisticLogger implements MetricStatisticProcessor{
    @Override
    public void process(MetricStatistic statistic) {
        synchronized(this) {
            log.info("----------------- Metric Statistic Record -----------------");
            log.info("Metric Id: {}", statistic.getId());
            log.info("Metric max: {}", statistic.getMax());
            log.info("Metric min: {}", statistic.getMin());
            log.info("Metric total: {}", statistic.getSum());
            log.info("Metric average: {}", statistic.getAvg());
            log.info("Metric median: {}", statistic.getMedian());
            log.info("-----------------------------------------------------------");
        }
    }
}
