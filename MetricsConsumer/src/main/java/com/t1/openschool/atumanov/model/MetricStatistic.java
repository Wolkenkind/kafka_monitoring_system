package com.t1.openschool.atumanov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "metrics_stat")
public class MetricStatistic implements Persistable<String> {
    @Id
    @Column("metric_id")
    private String id;
    @Column("metric_max")
    private float max;
    @Column("metric_min")
    private float min;
    @Column("metric_sum")
    private float sum;
    @Column("metric_avg")
    private float avg;
    @Column("metric_median")
    private float median;

    @Transient
    private boolean isNotCreatedInDb = true;

    public MetricStatistic(String id, float max, float min, float sum, float avg, float median) {
        this.id = id;
        this.max = max;
        this.min = min;
        this.sum = sum;
        this.avg = avg;
        this.median = median;
    }

    @Override
    public boolean isNew() {
        return isNotCreatedInDb;
    }
}
