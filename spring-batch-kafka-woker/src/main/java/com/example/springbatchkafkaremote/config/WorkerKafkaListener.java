package com.example.springbatchkafkaremote.config;

import com.example.springbatchkafkaremote.PartitionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkerKafkaListener {

    private final JobLauncher jobLauncher;
    private final Job workerJob; // make sure config is scanned

    @KafkaListener(topics = "partitions", groupId = "worker-group")
    public void receivePartition(PartitionRequest request) throws Exception {
        System.out.println("Received partition: " + request.getPartitionId());

        JobParameters params = new JobParametersBuilder()
                .addLong("start", request.getStart())
                .addLong("end", request.getEnd())
                .addString("partitionId", request.getPartitionId())
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(workerJob, params);
    }
}