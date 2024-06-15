package com.t1.openschool.atumanov.service;

import com.t1.openschool.atumanov.event.MetricDataReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MetricsDataKafkaListener {

    public static final String METRICS_TOPIC = "metrics-topic";

    private final ApplicationEventPublisher eventPublisher;

    @KafkaListener(id = "metricsListener", groupId = "metricsListeners", topics = METRICS_TOPIC, concurrency = "${metricsListener.concurrency:1}")
    public void listen(ConsumerRecord<String, String> record) {
        String metricId = record.key();
        Float metricValue;
        try {
            metricValue = Float.valueOf(record.value());
        } catch (NumberFormatException e) {
            log.error("Couldn't parse value for metric '{}'", metricId);
            throw new RuntimeException("Error reading record payload");
        }
        eventPublisher.publishEvent(new MetricDataReadEvent(metricId, metricValue));
    }
}
