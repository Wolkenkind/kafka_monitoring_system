package com.t1.openschool.atumanov.repository;

import com.t1.openschool.atumanov.model.MetricData;
import com.t1.openschool.atumanov.model.MetricStatistic;
import com.t1.openschool.atumanov.repository.MetricDataRepository;
import com.t1.openschool.atumanov.repository.MetricStatisticRepository;
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
public class MetricStatisticRepositoryTest {

    @Autowired
    MetricStatisticRepository repository;

    public static final String TESTNAME = "test";
    public static final String EDITED = "edited";

    @BeforeAll
    static void setup(@Autowired DatabaseClient client) {
        client.sql("INSERT INTO metrics_stat(metric_id, metric_max, metric_min, metric_sum, metric_avg, metric_median) VALUES ('" + TESTNAME + "',10,2,3,4,5)")
                .fetch()
                .rowsUpdated()
                .doOnNext(rowsUpdated -> System.out.println("setup: Rows inserted successfully"))
                .block();
    }

    @Test
    public void createRecords() {
        long count = repository.findAll().toStream().count();
        Flux<MetricStatistic> msFlux = Flux.just(new MetricStatistic("m1", 5f, 5f, 5f, 5f, 5f), new MetricStatistic("m2", 10f, 10f, 10f, 10f, 10f))
                .flatMap(execution -> repository.save(execution));

        System.out.println("createRecords: Going to create records...");
        StepVerifier.create(msFlux).expectNextCount(2).verifyComplete();

        System.out.println("createRecords: Going to read records...");
        Flux<MetricStatistic> all = repository.findAll();

        StepVerifier.create(all).expectNextCount(2 + count).verifyComplete();
    }

    @Test
    @Order(1)
    public void readRecord() {
        Mono<MetricStatistic> msFlux = repository.findById(TESTNAME);
        System.out.println("readRecord: Going to read record...");
        StepVerifier.create(msFlux).expectNextCount(1).verifyComplete();
        System.out.println("readRecord: Going to check record name...");
        StepVerifier.create(msFlux)
                .expectNextMatches(next -> next.getId().equals(TESTNAME)
                                            && next.getMax() == 10f
                                            && next.getMin() == 2f
                                            && next.getSum() == 3f
                                            && next.getAvg() == 4f
                                            && next.getMedian() == 5f)
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void updateRecord() {
        MetricStatistic metricStatistic = null;
        System.out.println("updateRecord: Going to read record...");
        try {
            metricStatistic = repository.findById(TESTNAME).block(Duration.ofMillis(20000));
        } catch (Exception e) {
            System.out.println("updateRecord: Exception during getting record from database: " + e.getMessage());
            Assertions.fail("Error reading record");
        }
        metricStatistic.setId(EDITED);
        System.out.println("updateRecord: Going to save edited record...");
        Mono<MetricStatistic> mdMono = repository.save(metricStatistic);
        StepVerifier.create(mdMono).expectNextMatches(next -> next.getId().equals(EDITED)).verifyComplete();
    }

    @Test
    @Order(3)
    public void deleteRecord() {
        Mono<Void> msMono = repository.deleteById(EDITED);
        System.out.println("deleteRecord: Going to delete record...");
        StepVerifier.create(msMono).verifyComplete();
        System.out.println("deleteRecord: Going to check on deleted record...");
        Mono<MetricStatistic> mdMonoCheck = repository.findById(EDITED);
        StepVerifier.create(mdMonoCheck).verifyComplete();
    }

    @AfterAll
    static void tearDown(@Autowired DatabaseClient client) {
        client.sql("DROP TABLE metrics_stat")
                .fetch()
                .rowsUpdated()
                .doOnNext(rowsUpdated -> System.out.println("tearDown: Table deleted successfully"))
                .block();
    }

}
