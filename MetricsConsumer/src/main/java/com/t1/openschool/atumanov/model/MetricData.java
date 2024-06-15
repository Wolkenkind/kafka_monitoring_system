package com.t1.openschool.atumanov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "metrics")
public class MetricData {
    @Id
    private Long id; // id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Column("metric_id")
    private String metricId;
    @Column("metric_value")
    private Float metricValue;
    @Column("auto_ts")
    private LocalDateTime dataTimestamp; // auto_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

    public MetricData(Long id, String metricId, Float metricValue, LocalDateTime dataTimestamp) {
        this.id = id;
        this.metricId = metricId;
        this.metricValue = metricValue;
        this.dataTimestamp = dataTimestamp;
    }

    public MetricData(String metricId, Float metricValue) {
        this.metricId = metricId;
        this.metricValue = metricValue;
    }
}
