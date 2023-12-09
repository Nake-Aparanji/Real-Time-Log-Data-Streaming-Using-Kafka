package com.example.LogDataStreaming;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogMessageConsumer {

    @Autowired
    private MessageStorage storage; // This will be used to store messages for the web view

    //private List<Long> latencies = new ArrayList<>();
    //private static final int MESSAGE_COUNT = 50; // Number of messages

    @KafkaListener(topics = "log-data-topic", groupId = "log-consumer-group")
    public void listen(String message) {
        long startTime = System.currentTimeMillis(); // Start time

        storage.put(message);

        long endTime = System.currentTimeMillis(); // End time
        long latency = endTime - startTime; // Calculate latency

        System.out.println("Received message: " + message);
        System.out.println("Processing latency: " + latency + " ms");

        //latencies.add(latency);

        /*if (latencies.size() == MESSAGE_COUNT) {
            printAverageLatency();
        }*/
    }

    /*private void printAverageLatency() {
        long sum = latencies.stream().mapToLong(Long::longValue).sum();
        double average = (double) sum / latencies.size();
        System.out.println("Average latency for " + MESSAGE_COUNT + " messages: " + average + " ms");

        // Reset the list for next batch of messages
        latencies.clear();
    }*/
}