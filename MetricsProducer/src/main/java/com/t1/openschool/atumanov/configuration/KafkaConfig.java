package com.t1.openschool.atumanov.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    public static final String METRICS_TOPIC = "metrics-topic";

    @Value("${homeworkApp.metricsTopicPartitions:1}")
    private int partitions;
    @Value("${homeworkApp.metricsTopicReplication:1}")
    private short replicas;

    @Bean
    public NewTopic metricsTopic() {
        return new NewTopic(METRICS_TOPIC, partitions, replicas);
    }

    @Bean
    public NewTopic dltTopic() {
        return new NewTopic(METRICS_TOPIC + ".DLT", partitions, replicas);
    }

    @Bean
    public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 3));
    }
}
