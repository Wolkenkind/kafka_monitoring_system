package com.t1.openschool.atumanov.listener;

import com.t1.openschool.atumanov.controller.MetricStatisticProcessor;
import com.t1.openschool.atumanov.event.MetricDataReadEvent;
import com.t1.openschool.atumanov.model.MetricData;
import com.t1.openschool.atumanov.model.MetricStatistic;
import com.t1.openschool.atumanov.service.MetricDataService;
import com.t1.openschool.atumanov.service.MetricStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricDataInputHandler {

    private final MetricStatisticService statisticService;
    private final MetricDataService dataService;

    @Autowired
    private List<MetricStatisticProcessor> processors;

    @Async
    @EventListener
    public void onDataInput(MetricDataReadEvent event) {
        String metricId = event.getMetricKey();
        //save metric data to DB
        //blocking here 'cause we read right after
        dataService.save(new MetricData(metricId, event.getMetricValue())).block();
        List<MetricData> metricData = dataService.findById(metricId).toStream().toList();
        MetricStatistic statistic = statisticService.gatherStatisticById(metricId, metricData);
        for (MetricStatisticProcessor processor: processors) {
            processor.process(statistic);
        }
    }
}
