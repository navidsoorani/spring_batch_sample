package com.example.springbatchkafkaremote.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableBatchProcessing
public class WorkerBatchConfig {

    @Bean
    public Job workerJob(JobRepository jobRepository, Step workerStep) {
        return new JobBuilder("workerJob", jobRepository)
                .start(workerStep)
                .build();
    }

    @Bean
    public Step workerStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager) {

        return new StepBuilder("workerStep", jobRepository)
                .tasklet((contribution, ctx) -> {
                    JobParameters jobParameters = contribution.getStepExecution()
                            .getJobParameters();

                    long start = jobParameters.getLong("start");
                    long end = jobParameters.getLong("end");
                    String partitionId = jobParameters.getString("partitionId");

                    System.out.printf("Worker processing partition %s: %d..%d%n",
                            partitionId, start, end);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


}