package com.example.push_notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/subscribe")
    public void subscribe(String userId, String endpoint) {
        notificationService.subscribe(userId, endpoint);
    }

    @RequestMapping("/unsubscribe")
    public void unsubscribe(String userId, String endpoint) {
        notificationService.unsubscribe(userId, endpoint);
    }

    @RequestMapping("/send")
    public void sendNotification(String userId, String content) {
        notificationService.sendNotification(userId, content);
    }

    @MessageMapping("/broadcast")
    @SendTo("/topic/public")
    public String broadcast(String content) {
        notificationService.sendNotification("all", content);
        return content;
    }
}
