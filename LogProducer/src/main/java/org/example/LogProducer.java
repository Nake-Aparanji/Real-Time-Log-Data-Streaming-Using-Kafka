package org.example;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.Random;

public class LogProducer {
    private static final String[] LOG_LEVELS = {"INFO", "DEBUG", "WARNING", "ERROR", "CRITICAL"};
    private static final String[] EVENT_TYPES = {"Authentication", "Access", "Error", "Security"};
    private static final String[] SOURCES = {"127.0.0.1", "example.com", "192.168.1.1"};
    private static final String[] USERS = {"user1", "user2", "admin", "guest"};
    private static final String[] REQUESTS = {"/api/resource", "/login", "/page", "/download"};
    private static final int[] STATUS_CODES = {200, 400, 404, 500};
    private static final String[] ERROR_DETAILS = {"NullPointerException", "TimeoutException", "InvalidCredentials"};

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "log-data-topic";

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        long startTime = System.currentTimeMillis(); // Record start time for Ingestion Rate calculation

        try {
            for (int i = 0; i < 1000; i++) {
                // Simulate log entry
                String logEntry = generateLogEntry();

                // Send log entry to the Kafka topic
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", logEntry);

                long recordStartTime = System.currentTimeMillis(); // Record start time for Ingestion Rate calculation

                producer.send(record, new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            long endTime = System.currentTimeMillis(); // Record end time for Ingestion Rate calculation
                            double ingestionRate = 1000.0 / (endTime - startTime); // Calculate Ingestion Rate (records per second)
                            System.out.println("Log entry sent successfully. Offset: " + metadata.offset() +
                                    ", Ingestion Rate: " + ingestionRate + " records/second");
                        }
                    }
                });

                // Introduce a delay between log entries for simulation purposes
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    private static String generateLogEntry() {
        Random random = new Random();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String logLevel = LOG_LEVELS[random.nextInt(LOG_LEVELS.length)];
        String message = "This is a sample log message.";
        String eventType = EVENT_TYPES[random.nextInt(EVENT_TYPES.length)];
        String source = SOURCES[random.nextInt(SOURCES.length)];
        String user = USERS[random.nextInt(USERS.length)];
        String request = REQUESTS[random.nextInt(REQUESTS.length)];
        int statusCode = STATUS_CODES[random.nextInt(STATUS_CODES.length)];
        String sessionID = "session-" + random.nextInt(1000);
        String errorDetails = "";

        // Introduce errors for some log entries
        if (random.nextBoolean()) {
            errorDetails = "Error occurred: " + ERROR_DETAILS[random.nextInt(ERROR_DETAILS.length)];
        }

        // Format the log entry
        return String.format(
                "Timestamp: %s, LogLevel: %s, Message: %s, EventType: %s, Source: %s, User: %s, Request: %s, " +
                        "StatusCode: %d, SessionID: %s, ErrorDetails: %s",
                timestamp, logLevel, message, eventType, source, user, request, statusCode, sessionID, errorDetails
        );
    }
}
