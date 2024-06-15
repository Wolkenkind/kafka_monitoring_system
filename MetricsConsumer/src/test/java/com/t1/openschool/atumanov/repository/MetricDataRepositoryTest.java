package com.t1.openschool.atumanov.repository;

import com.t1.openschool.atumanov.model.MetricData;
import com.t1.openschool.atumanov.repository.MetricDataRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@ComponentScan(basePackages = {"com.t1.openschool.atumanov.model", "com.t1.openschool.atumanov.repository"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetricDataRepositoryTest {

    @Autowired
    MetricDataRepository repository;

    public static final String TESTNAME = "test";
    public static final String EDITED = "edited";

    @BeforeAll
    static void setup(@Autowired DatabaseClient client) {
        client.sql("INSERT INTO metrics(metric_id, metric_value) VALUES ('" + TESTNAME + "',100)")
                .fetch()
                .rowsUpdated()
                .doOnNext(rowsUpdated -> System.out.println("setup: Rows inserted successfully"))
                .block();
    }

    @Test
    public void createRecords() {
        long count = repository.findAll().toStream().count();
        Flux<MetricData> mdFlux = Flux.just(new MetricData("m1", 5f), new MetricData("m2", 10f))
                .flatMap(execution -> repository.save(execution));

        System.out.println("createRecords: Going to create records...");
        StepVerifier.create(mdFlux).expectNextCount(2).verifyComplete();

        System.out.println("createRecords: Going to read records...");
        Flux<MetricData> all = repository.findAll();

        StepVerifier.create(all).expectNextCount(2 + count).verifyComplete();
    }

    @Test
    @Order(1)
    public void readRecord() {
        Mono<MetricData> mdFlux = repository.findById(1L);
        System.out.println("readRecord: Going to read record...");
        StepVerifier.create(mdFlux).expectNextCount(1).verifyComplete();
        System.out.println("readRecord: Going to check record name...");
        StepVerifier.create(mdFlux)
                .expectNextMatches(next -> next.getMetricId().equals(TESTNAME))
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void updateRecord() {
        MetricData metricData = null;
        System.out.println("updateRecord: Going to read record...");
        try {
            metricData = repository.findById(1L).block(Duration.ofMillis(20000));
        } catch (Exception e) {
            System.out.println("updateRecord: Exception during getting record from database: " + e.getMessage());
            Assertions.fail("Error reading record");
        }
        metricData.setMetricId(EDITED);
        System.out.println("updateRecord: Going to save edited record...");
        Mono<MetricData> mdMono = repository.save(metricData);
        StepVerifier.create(mdMono).expectNextMatches(next -> next.getMetricId().equals(EDITED)).verifyComplete();
    }

    @Test
    @Order(3)
    public void deleteRecord() {
        Mono<Void> mdMono = repository.deleteById(1L);
        System.out.println("deleteRecord: Going to delete record...");
        StepVerifier.create(mdMono).verifyComplete();
        System.out.println("deleteRecord: Going to check on deleted record...");
        Mono<MetricData> mdMonoCheck = repository.findById(1L);
        StepVerifier.create(mdMonoCheck).verifyComplete();
    }

    @AfterAll
    static void tearDown(@Autowired DatabaseClient client) {
        client.sql("DROP TABLE metrics")
                .fetch()
                .rowsUpdated()
                .doOnNext(rowsUpdated -> System.out.println("tearDown: Table deleted successfully"))
                .block();
    }
}
