Flow explanation

Master application:
Has partitionJob → masterStep.
Splits work into chunks (PartitionRequest), e.g., start/end indices.
Sends each partition request as a message to Kafka.

Kafka:
Topic partitions with multiple partitions (0..N).
Distributes messages among worker consumers.

Worker application:
Has a WorkerKafkaListener consuming messages.
Each message triggers a Job launch.
Step (workerStep) processes the chunk.

Updates Batch tables (e.g., BATCH_JOB_EXECUTION, BATCH_STEP_EXECUTION).

Scaling:
Multiple worker instances can consume from the same topic.
Kafka partitions ensure load balancing.

Spring Batch JobRepository ensures job executions are tracked.
