package com.t1.openschool.atumanov.event;

import lombok.Data;

@Data
public class MetricDataReadEvent {

    String metricKey;
    Float metricValue;

    public MetricDataReadEvent(String key, Float value) {
        metricKey = key;
        metricValue = value;
    }
}
