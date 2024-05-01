package com.message.chatservice.service.impl;

import com.message.chatservice.model.dto.NotificationDTO;
import com.message.chatservice.model.dto.pageable.NotificationPageable;
import com.message.chatservice.model.entity.Notification;
import com.message.chatservice.repository.NotificationRepo;
import com.message.chatservice.repository.specification.NotificationSpec;
import com.message.chatservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.message.chatservice.constant.NotificationStatus.SYSTEM;
import static com.message.chatservice.utils.DateTimeUtil.today;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final NotificationSpec notificationSpec;

    @Override
    @Transactional
    public void saveNotification(Notification notification) {
        notification.setCreatedDate(today());
        notification.setUpdatedDate(today());
        notificationRepo.save(notification);
    }

    @Override
    public NotificationPageable getNotificationsByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("updatedDate").descending());
        Page<Notification> notificationPage = notificationRepo.findAll(
                Specification.where(notificationSpec.hasStatus(SYSTEM)).or(notificationSpec.hasEmail(email)), pageable);
        List<NotificationDTO> notificationDTOS = notificationPage.getContent().stream()
                .map(Notification::convertToDTO)
                .collect(Collectors.toList());
        return NotificationPageable.builder()
                .hasNext(notificationPage.hasNext())
                .currentPage(page)
                .content(notificationDTOS)
                .totalPage(notificationPage.getTotalPages())
                .total(notificationPage.getTotalElements())
                .build();
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }

//    public void when(NotificationDTO notificationDTO){
//        switch (notificationDTO.getType()){
//            case ROOM_ACTION_NOTIFICATION:
//
//                break;
//            default:
//                log.info("default");
//        }
//
//    }
//    private String buildPayload(NotificationDTO notificationDTO){
//        NotificationPayload payload = NotificationPayload.builder()
//                .avatar(notificationDTO.getAvatar())
//                .content(notificationDTO.getContent())
//                .title(notificationDTO.getTitle())
//                .build();
//        return serializeToJsonString(payload);
//    }
}
