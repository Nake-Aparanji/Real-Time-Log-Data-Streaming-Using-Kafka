package org.example;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import java.util.HashMap;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogConsumer {

    private static final Pattern LOG_LEVEL_PATTERN = Pattern.compile("LogLevel: (\\w+),");
    private static final Pattern EVENT_TYPE_PATTERN = Pattern.compile("EventType: (\\w+),");

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String groupId = "log-consumer-group";
        String topic = "log-data-topic";

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create Kafka consumer
        Consumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the "log-data-topic"
        consumer.subscribe(Collections.singletonList(topic));

        // Initialize counters
        Map<String, Integer> logLevelCounts = new HashMap<>();
        Map<String, Integer> eventTypeCounts = new HashMap<>();

        // Poll for messages
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                // Process received records
                records.forEach(record -> {
                    long startTime = System.currentTimeMillis(); // Record start time for Latency calculation

                    // Increment log level count
                    String logLevel = extractLogLevel(record.value());
                    logLevelCounts.put(logLevel, logLevelCounts.getOrDefault(logLevel, 0) + 1);

                    // Increment event type count
                    String eventType = extractEventType(record.value());
                    eventTypeCounts.put(eventType, eventTypeCounts.getOrDefault(eventType, 0) + 1);

                    // Print consumed log entry
                    System.out.println("Consumed log entry:");
                    System.out.println("Key: " + record.key());
                    System.out.println("Value: " + record.value());
                    System.out.println("--------------------------");

                    long endTime = System.currentTimeMillis(); // Record end time for Latency calculation
                    long latency = endTime - startTime; // Calculate Latency

                    // Print analysis results and latency
                    System.out.println("Log Level Counts: " + logLevelCounts);
                    System.out.println("Event Type Counts: " + eventTypeCounts);
                    System.out.println("Latency: " + latency + " milliseconds");
                });
            }
        } finally {
            consumer.close();
        }
    }
    private static String extractLogLevel(String logEntry) {
        Matcher matcher = LOG_LEVEL_PATTERN.matcher(logEntry);
        return matcher.find() ? matcher.group(1) : "Unknown";
    }

    private static String extractEventType(String logEntry) {
        Matcher matcher = EVENT_TYPE_PATTERN.matcher(logEntry);
        return matcher.find() ? matcher.group(1) : "Unknown";
    }
}
