package com.t1.openschool.atumanov.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public MetricsEndpoint metricsEndpoint(MeterRegistry registry) {
        return new MetricsEndpoint(registry);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
