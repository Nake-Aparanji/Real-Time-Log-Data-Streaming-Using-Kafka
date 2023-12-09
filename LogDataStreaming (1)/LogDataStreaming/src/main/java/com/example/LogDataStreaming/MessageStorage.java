package com.example.LogDataStreaming;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessageStorage {

    private ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();

    public void put(String message) {
        messages.add(message);
    }

    public ConcurrentLinkedQueue<String> getMessages() {
        return messages;
    }
}