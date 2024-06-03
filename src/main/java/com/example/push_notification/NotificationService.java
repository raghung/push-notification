package com.example.push_notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final Map<String, List<String>> endpoints = new HashMap<>();
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void subscribe(String userId, String endpoint) {
        if (!endpoints.containsKey(userId)) {
            endpoints.put(userId, new ArrayList<>());
        }
        if (!endpoints.get(userId).contains(endpoint)) {
            endpoints.get(userId).add(endpoint);
        }
    }

    public void unsubscribe(String userId, String endpoint) {
        if (endpoints.containsKey(userId)) {
            endpoints.get(userId).remove(endpoint);
        }
    }

    public List<String> getEndpoints(String userId) {
        return endpoints.get(userId);
    }

    public void sendNotification(String userId, String content) {
        if (endpoints.containsKey(userId)) {
            for (String endpoint : endpoints.get(userId)) {
                messagingTemplate.convertAndSend("/topic/" + endpoint, content);
            }
        } else if (userId.equals("all")) {
            // Iterate through all endpoints
            for (String user : endpoints.keySet()) {
                // Send message to each endpoint
                for (String endpoint : endpoints.get(user)) {
                    messagingTemplate.convertAndSend("/topic/" + endpoint, content);
                }
            }
        }
    }
}
