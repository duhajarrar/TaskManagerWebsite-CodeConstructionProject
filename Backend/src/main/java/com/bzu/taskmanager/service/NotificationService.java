package com.bzu.taskmanager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bzu.taskmanager.model.*;
import com.bzu.taskmanager.repository.*;

import java.util.Date;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    
    public void sendNotification(Task task, String userId, String message) {
        Notification notification = new Notification(userId, message, new Date());
        notificationRepository.save(notification);
    }
}
