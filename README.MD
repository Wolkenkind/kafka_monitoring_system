# Spring Kafka Homework Project

This project is a homework for [T1 Open School of Java development][1], demonstrating use of [Apache Kafka][2] as a core of a monitoring system. It consists of two microservices, Producer and Consumer, both developed using [Spring Boot][3] and [Spring Kafka][4]. API complies with [OpenAPI Specification][5] v.[3.0.0][6] and the code is generated with help of [OpenAPI Generator plugin for Gradle][7]. The API definition files can be found in `/scr/main/resources` folders of modules.

API documentation is generated using [springdoc-openapi][8] java library and can be accessible locally 

for Producer as [JSON][9] or [Swagger UI][10]

for Consumer as  [JSON][15] or [Swagger UI][16]

after running the application.

Producer service also uses [Spring Actuator][11] and [Micrometer][12] as a source of metric data. Consumer service employs [PostgreSQL][13] as a database to store metric and statistic data with help of [Project Reactor][14] to write records in asynchronous, non-blocking manner.

## Prerequisites

In order to run this project...

- you must have access to Apache Kafka broker and be able to create/subscribe to a topic on it 
- you must have access to PostgreSQL instance which holds a database named `postgres` and user `postgres` (also database named `test` for running tests). You can modify `application.yaml` file to use your specific settings and names
- to create any statistic records, table `metrics` must contain metric data. You can populate the table manually using Producer's API or you can wait till Producer populates it with metric data produced by Spring Actuator (for that, the setting `homeworkApp.actuatorMetricsSend.enabled` in `application.yaml` must be set to `true`)

## Details and use instructions

Start the Metrics Producer application and use API to provide some metric data manually or use `ActuatorMetricsSender` class which will send some metrics provided by Spring Actuator every 30 seconds. Check the application property `homeworkApp.actuatorMetricsSend.enabled`, it must be set to `true` for `ActuatorMetricsSender` class to work.

Start the Metrics Consumer application and use API to get the metric statistics data. Also check the console for the incoming metrics data.

Further details and instructions are to find in corresponding `README.MD` files for services projects.

Project features `docker-compose.yaml` that can be used to quickly set up an environment, featuring Apache Kafka and PostgreSQL instances with their standard respective ports. Also an [Adminer][17] instance is available on port `8088`.

[1]: https://t1.ru/internship/item/otkrytaya-shkola-dlya-java-razrabotchikov/
[2]: https://kafka.apache.org/
[3]: https://spring.io/projects/spring-boot
[4]: https://spring.io/projects/spring-kafka
[5]: https://www.openapis.org/
[6]: https://swagger.io/specification/v3/
[7]: https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin
[8]: https://springdoc.org/
[9]: http://localhost:18088/v3/api-docs
[10]: http://localhost:18088/swagger-ui/index.html
[11]: https://docs.spring.io/spring-boot/reference/actuator/
[12]: https://micrometer.io/
[13]: https://www.postgresql.org/
[14]: https://projectreactor.io/
[15]: http://localhost:28088/v3/api-docs
[16]: http://localhost:28088/swagger-ui/index.html
[17]: https://www.adminer.org/