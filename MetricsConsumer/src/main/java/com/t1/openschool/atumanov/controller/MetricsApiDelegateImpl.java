package com.t1.openschool.atumanov.controller;

import com.t1.openschool.atumanov.MetricRecord;
import com.t1.openschool.atumanov.model.MetricData;
import com.t1.openschool.atumanov.model.MetricStatistic;
import com.t1.openschool.atumanov.service.MetricDataService;
import com.t1.openschool.atumanov.service.MetricStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MetricsApiDelegateImpl implements com.t1.openschool.atumanov.controller.MetricsApiDelegate {

    private final MetricStatisticService statisticService;
    private final MetricDataService dataService;

    @Override
    public ResponseEntity<List<MetricRecord>> getAllMetrics() {
        List<MetricStatistic> statistics = statisticService.findAll().toStream().toList();
        if(statistics.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            List<MetricData> data = dataService.findAllMostRecentMetrics().toStream().toList();
            List<MetricRecord> records = new ArrayList<>();
            for(MetricStatistic statistic: statistics) {
                String id = statistic.getId();
                records.add(new MetricRecord(id,
                        data.stream().filter(item -> item.getMetricId().equals(id)).findFirst().orElseThrow().getMetricValue(),
                        statistic.getMax(),
                        statistic.getMin(),
                        statistic.getAvg(),
                        statistic.getSum(),
                        statistic.getMedian()));
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<MetricRecord> getMetricById(String id) {
        MetricStatistic statistic = statisticService.findById(id).block();
        if(statistic == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            MetricData data = dataService.findMostRecentByMetricId(id).block();
            MetricRecord result = new MetricRecord(id,
                    data.getMetricValue(),
                    statistic.getMax(),
                    statistic.getMin(),
                    statistic.getAvg(),
                    statistic.getSum(),
                    statistic.getMedian());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
