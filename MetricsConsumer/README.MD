# Spring Kafka Homework Project (Metrics Consumer)

This project is part of the homework project [T1 Open School of Java development][1], demonstrating use of [Apache Kafka][2] as a core of a monitoring system. Please refer to `README.MD` of the parent project for high level details.

## Prerequisites

In order to run this project...

- you must have access to Apache Kafka broker and be able to subscribe to a topic on it
- you must have access to PostgreSQL instance which holds a database named `postgres` and user `postgres` (also database named `test` for running tests). You can modify `application.yaml` file to use your specific settings and names
- it is meant to function in connection with Metrics Producer service, which sends metric data through Apache Kafka topic

## Kafka configuration

Service subscribes to a topic named `metrics-topic` as Consumer-Group with an id of `metricsListeners`. Number of simultaneously working Kafka consumers can be controlled by application property `metricsListener.concurrency` and defaults to 1 if property is missing. 

## Use instructions and API

Application can be started as usual Spring Boot application through the `HomeworkKafkaConsumerApplication` class as entry point or deployed to the server (configured port `28088`). After initialization of Spring context, Kafka consumer(s) will subscribe to topic `metrics-topic` and read records according to their offsets, managed by Kafka broker.

Every time a record is read, an `MetricDataReadEvent` event is fired. `MetricDataInputHandler` class is responsible for handling this event and processing the metric data. The metric data is saved to the database in the table `metrics`, after that all the data for incoming metric is read from database to create a new statistic record. Further processing of statistic can be set up by adding or removing implementations of `MetricStatisticProcessor` interface. Currently, two types of `MetricStatisticProcessor` are implemented and injected into `MetricDataInputHandler`:
- **MetricStatisticLogger**: this class logs the statistic record to console
- **MetricStatisticDatabaseWriter**: this class writes the statistic record to the table `metrics_stat` in asynchronous, non-blocking manner

To add new processing of statistic records, just implement `MetricStatisticProcessor` interface and annotate implementation as `@Service` so that Spring can find and inject it.

API enables you to access statistic records and last written value of specified metric or all metrics. Most comfortable way to read the API documentation is through Swagger UI. Start the application and use the [link][3].

---
- `GET` /metrics

Returns data about submitted metrics from monitoring system
- parameters:

    *none*
- responses:
    - `200` information about all metrics submitted to the system
        - ***array*** of:
          - **string** `"metricId` example: *some.metric.name*
          - **number** `metricLastValue` example: *42*
          - **number** `metricMax` example: *21.1134*
          - **number** `metricMin` example: *0.211134*
          - **number** `metricMean` example: *24.1*
          - **number** `metricSum` example: *100500*
          - **number** `metricMedian` example: *12*
    - `404` resource not found
    - `500` internal error

---
- `GET` /metrics/{id}

Returns data about specified metric from monitoring system
- parameters:
  - `id` Metric id
- responses:
    - `200` information about all metrics submitted to the system
        - **string** `"metricId` example: *some.metric.name*
        - **number** `metricLastValue` example: *42*
        - **number** `metricMax` example: *21.1134*
        - **number** `metricMin` example: *0.211134*
        - **number** `metricMean` example: *24.1*
        - **number** `metricSum` example: *100500*
        - **number** `metricMedian` example: *12*
    - `404` resource not found
    - `500` internal error

---



[1]: https://t1.ru/internship/item/otkrytaya-shkola-dlya-java-razrabotchikov/
[2]: https://kafka.apache.org/
[3]: http://localhost:28088/swagger-ui/index.html