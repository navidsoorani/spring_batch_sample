package com.example.springbatchkafkaremote.config;

import com.example.springbatchkafkaremote.PartitionRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Map;


@Configuration
@EnableBatchProcessing
public class MasterBatchConfig {

    @Bean
    public Job partitionJob(JobRepository jobRepository, Step masterStep) {
        return new JobBuilder("partitionJob", jobRepository)
                .start(masterStep)
                .build();
    }

    @Bean
    public Step masterStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           KafkaTemplate<String, PartitionRequest> kafkaTemplate) {

        return new StepBuilder("masterStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    int gridSize = 4;
                    int total = 100;
                    int chunk = total / gridSize;

                    for (int i = 0; i < gridSize; i++) {
                        int start = i * chunk + 1;
                        int end = (i == gridSize - 1) ? total : (i + 1) * chunk;

                        PartitionRequest req = new PartitionRequest();
                        req.setPartitionId("partition-" + i);

                        Map<String, Object> params = Map.of(
                                "start", start,
                                "end", end,
                                "partition", i
                        );

                        req.setJobParameters(params);

                        kafkaTemplate.send("partitions", req.getPartitionId(), req);

                        System.out.println("Sent partition: " + req.getPartitionId());
                    }

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}