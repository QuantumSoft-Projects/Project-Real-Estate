package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.entity.Notification;
import com.quantumsoft.realestate.servicei.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

@Autowired
    private NotificationService notificationService;
@Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send/{userId}")
    public ResponseEntity<String> sendNotification(@PathVariable Long userId) {
        Notification notification = notificationService.createNotification(
                userId,
                "New Property Alert",
                "A new property is listed in your area!",
                "NEW_PROPERTY"
        );

        if (notification == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create notification.");
        }

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
        return ResponseEntity.ok("Notification sent!");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
