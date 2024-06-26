package com.bzu.taskmanager.repository;
import com.bzu.taskmanager.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    // You can define additional custom query methods if needed
}

