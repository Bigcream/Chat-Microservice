package com.message.chatservice.service;

import com.message.chatservice.model.dto.pageable.NotificationPageable;
import com.message.chatservice.model.entity.Notification;

public interface NotificationService {

    void saveNotification(Notification notificationDTO);

    NotificationPageable getNotificationsByEmail(String email, int page, int size);

    void deleteNotification(Long id);
}
