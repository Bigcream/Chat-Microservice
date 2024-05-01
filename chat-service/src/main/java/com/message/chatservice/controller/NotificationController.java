package com.message.chatservice.controller;

import com.message.chatservice.model.dto.pageable.NotificationPageable;
import com.message.chatservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class NotificationController extends BaseController{

    private final NotificationService notificationService;

    @GetMapping("/notifications/{email}")
    public ResponseEntity<NotificationPageable> getNotifications(@PathVariable String email,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size){
        return new ResponseEntity<>(notificationService.getNotificationsByEmail(email, page, size), noCacheHeader, HttpStatus.OK);
    }

    @DeleteMapping("/notification/{id}")
    public ResponseEntity<Void> deleteNotifications(@PathVariable Long id){
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(noCacheHeader, HttpStatus.OK);
    }
}
