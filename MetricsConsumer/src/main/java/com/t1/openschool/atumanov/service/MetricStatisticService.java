package com.t1.openschool.atumanov.service;

import com.t1.openschool.atumanov.model.MetricData;
import com.t1.openschool.atumanov.model.MetricStatistic;
import com.t1.openschool.atumanov.repository.MetricStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MetricStatisticService {

    private final MetricStatisticRepository repository;

    public Mono<MetricStatistic> findById(String metricId) {
        return repository.findById(metricId);
    }

    public Mono<MetricStatistic> save(MetricStatistic statistic) {
        return repository.save(statistic);
    }

    public Flux<MetricStatistic> findAll() {
        return repository.findAll();
    }

    public MetricStatistic gatherStatisticById(String metricId, List<MetricData> dataList) {
        float max = (float) dataList.stream().mapToDouble(MetricData::getMetricValue).max().orElse(0);
        float min = (float) dataList.stream().mapToDouble(MetricData::getMetricValue).min().orElse(0);
        float sum = (float) dataList.stream().mapToDouble(MetricData::getMetricValue).sum();
        float avg = (float) dataList.stream().mapToDouble(MetricData::getMetricValue).average().orElse(0);
        float median = getMedian(dataList.stream().map(MetricData::getMetricValue).collect(Collectors.toList()));
        return new MetricStatistic(metricId, max, min, sum, avg, median);
    }

    //not most optimal implementation, but that's not the point here
    private float getMedian(List<Float> data) {
        Collections.sort(data);
        float median = data.get(data.size()/2);
        if(data.size() % 2 == 0) median = (median + data.get(data.size() / 2 - 1)) / 2;
        return median;
    }
}
