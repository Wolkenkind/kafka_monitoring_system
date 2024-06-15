package com.t1.openschool.atumanov.repository;

import com.t1.openschool.atumanov.model.MetricData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MetricDataRepository extends ReactiveCrudRepository<MetricData, Long> {
    Flux<MetricData> findByMetricId(String id);

    @Query("SELECT * FROM metrics WHERE metric_id=:metricId ORDER BY auto_ts DESC LIMIT 1")
    Mono<MetricData> findMostRecentByMetricId(String metricId);

    @Query("SELECT DISTINCT ON (metric_id) * FROM metrics ORDER BY metric_id, auto_ts DESC")
    Flux<MetricData> findAllMostRecentMetrics();
}
