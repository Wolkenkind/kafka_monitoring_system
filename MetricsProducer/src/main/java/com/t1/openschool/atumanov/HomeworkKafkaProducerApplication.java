package com.t1.openschool.atumanov;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@ComponentScan(
        basePackages = {"org.openapitools", "com.t1.openschool.atumanov" , "org.openapitools.configuration"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class HomeworkKafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeworkKafkaProducerApplication.class, args);
    }
}
