package com.example.springbatchkafkaremote;

import java.io.Serializable;
import java.util.Map;


public class PartitionRequest implements Serializable {
    private String partitionId;
    private Map<String, Object> jobParameters;

    private Long Start;
    private Long End;


    public String getPartitionId() { return partitionId; }
    public void setPartitionId(String partitionId) { this.partitionId = partitionId; }
    public Map<String, Object> getJobParameters() { return jobParameters; }
    public void setJobParameters(Map<String, Object> jobParameters) { this.jobParameters = jobParameters; }
}