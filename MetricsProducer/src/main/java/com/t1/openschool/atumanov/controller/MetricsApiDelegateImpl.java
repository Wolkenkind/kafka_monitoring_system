package com.t1.openschool.atumanov.controller;

import com.t1.openschool.atumanov.MetricBody;
import com.t1.openschool.atumanov.MetricPostAcceptedResponse;
import com.t1.openschool.atumanov.configuration.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MetricsApiDelegateImpl implements MetricsApiDelegate {

    private static final String ACCEPTED_RESPONSE = "Metric created and accepted for further processing";

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Override
    public ResponseEntity<MetricPostAcceptedResponse> postMetrics(MetricBody metricBody) {
        kafkaTemplate.send(KafkaConfig.METRICS_TOPIC, metricBody.getKey(), metricBody.getValue().toString());

        return new ResponseEntity<>(new MetricPostAcceptedResponse(ACCEPTED_RESPONSE), HttpStatus.ACCEPTED);
    }
}
