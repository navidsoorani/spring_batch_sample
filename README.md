Master-Worker Spring Batch with Kafka
+----------------+                       +----------------+
|   Master App   |                       |   Worker App   |
| (Spring Batch) |                       | (Spring Batch) |
+----------------+                       +----------------+
        |                                         |
        |         Partitioning Job                |
        |---------------------------------------->|
        |                                         |
        |  1. MasterStep divides work into        |
        |     PartitionRequests (start, end, id) |
        |                                         |
        |  2. Send PartitionRequest messages      |
        |     to Kafka Topic "partitions"        |
        |---------------------------------------->|
        |                                         |
        |                                         |
        |                                 +-------------------+
        |                                 | Kafka Topic       |
        |                                 | "partitions"      |
        |                                 | Partitions: 0..3 |
        |                                 +-------------------+
        |                                         |
        |                                         |
        |  3. Worker KafkaListener consumes       |
        |     PartitionRequest from Kafka        |
        |                                         |
        |--------------------------------------->|
        |                                         |
        |  4. Worker launches Job/Step            |
        |     with JobParameters from message    |
        |                                         |
        |  5. WorkerStep executes tasklet        |
        |     using start/end values             |
        |                                         |
        |  6. Writes progress to Spring Batch DB |
        |                                         |
        |                                         |
        +----------------------------------------+
