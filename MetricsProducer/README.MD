# Spring Kafka Homework Project (Metrics Producer)

This project is part of the homework project [T1 Open School of Java development][1], demonstrating use of [Apache Kafka][2] as a core of a monitoring system. Please refer to `README.MD` of the parent project for high level details.

## Prerequisites

In order to run this project...

- you must have access to Apache Kafka broker and be able to publish to a topic on it
- it is meant to function in connection with Metrics Consumer service, which reads metric data through Apache Kafka topic

## Kafka configuration

Service publishes metrics data in a "key-value" form to a topic named `metrics-topic`. Following settings in `application.yaml` can be used to control Kafka topic properties:
- **homeworkApp.metricsTopicPartitions** - controls number of partitions for the topic. If not found or set, defaults to 1
- **homeworkApp.metricsTopicReplication** - controls replication factor for the topic. If not found or set, defaults to 1
 > Note: replication factor for topic has sense in case of a Kafka cluster setup, which is not the case here 

## Use instructions and API

Application can be started as usual Spring Boot application through the `HomeworkKafkaProducerApplication` class as entry point or deployed to the server (configured port `18088`). After initialization of Spring context, one can manually submit metrics data through the API. Data received through the API is sent as "key-value" pairs to the Metrics Consumer service with means of Kafka topic `metrics-topic`.
Application properties in `application.yaml` file contain following setting: `homeworkApp.actuatorMetricsSend.enabled`. When this setting is set to `true`, the application will automatically send a set of metrics data that is provided by Spring Actuator to the API endpoint, scheduled to happen every 30 seconds.

Most comfortable way to read the API documentation is through Swagger UI. Start the application and use the [link][3].

---
- `POST` /metrics

Send application metric in key-value format. Only numeric values are currently supported
- parameters:

  *none*
- request body:

  **`key`**: *string* example: *some.metric.name*

  **`value`**: *number* example: *32*
- responses:
    - `202` metric created and accepted for further processing
        - **string** `information` example: *metric created and accepted for further processing*
    - `500` internal error

---




[1]: https://t1.ru/internship/item/otkrytaya-shkola-dlya-java-razrabotchikov/
[2]: https://kafka.apache.org/
[3]: http://localhost:18088/swagger-ui/index.html