package com.t1.openschool.atumanov.controller;

import com.t1.openschool.atumanov.MetricBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricDescriptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActuatorMetricsSender {

    private static final String[] METRICS = {"disk.free", "jvm.threads.started", "jvm.classes.loaded", "jvm.threads.daemon", "jvm.threads.live", "jvm.buffer.memory.used"};

    private final MetricsEndpoint metricsEndpoint;

    @Autowired
    private RestTemplate restTemplate;
    @Value("${server.port}")
    private int port;
    private String url;

    @Scheduled(fixedRate = 30000)
    @ConditionalOnProperty(value = "homeworkApp.actuatorMetricsSend.enabled", havingValue = "true", matchIfMissing = false)
    public void sendMetrics() {
        log.info("Sending metrics from actuator...");
        url = "http://localhost:" + port + "/metrics";
        for (String metricName: METRICS) {
            MetricDescriptor metricDescriptor = metricsEndpoint.metric(metricName, null);
            var response = restTemplate.postForEntity(url, new MetricBody(metricDescriptor.getName(), metricDescriptor.getMeasurements().get(0).getValue().floatValue()), String.class);
            if(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                log.error("Error sending metric '{}': status code {}", metricDescriptor.getName(), response.getStatusCode());
            }
        }
        log.info("Done.");
    }
}
