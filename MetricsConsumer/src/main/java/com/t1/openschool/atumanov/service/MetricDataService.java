package com.t1.openschool.atumanov.service;

import com.t1.openschool.atumanov.model.MetricData;
import com.t1.openschool.atumanov.repository.MetricDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MetricDataService {

    private final MetricDataRepository repository;

    public Flux<MetricData> findById(String metricId) {
        return repository.findByMetricId(metricId);
    }

    public Mono<MetricData> save(MetricData data) {
        return repository.save(data);
    }

    public Mono<MetricData> findMostRecentByMetricId(String metricId) {
        return repository.findMostRecentByMetricId(metricId);
    }

    public Flux<MetricData> findAllMostRecentMetrics() {
        return repository.findAllMostRecentMetrics();
    }
}
