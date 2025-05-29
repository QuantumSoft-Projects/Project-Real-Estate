package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Notification;

import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.enums.NotificationType;
import com.quantumsoft.realestate.repository.NotificationRepository;
import com.quantumsoft.realestate.repository.UserRepository;

import com.quantumsoft.realestate.servicei.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
@Autowired
    private NotificationRepository notificationRepository;
@Autowired
    private UserRepository userRepository;

    @Override
    public Notification createNotification(Long userId, String title, String message, String type) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.valueOf(type));
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        notification.setUser(user);

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        notification.setRead(true);  // Use setRead() instead of setIsRead()
        notificationRepository.save(notification);
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(Notification notification) {
        String topic = "/topic/notifications/" + notification.getUser().getId();
        messagingTemplate.convertAndSend("/topic/notifications/{userId}");

    }

}
