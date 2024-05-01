package com.message.chatservice.kafka.consumer;

import com.message.chatservice.kafka.producer.SocketProducer;
import com.message.chatservice.model.entity.Notification;
import com.message.chatservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.message.chatservice.constant.KafkaTopic.FRIEND_NOTIFICATION;
import static com.message.chatservice.constant.KafkaTopic.GROUP_ACTION;
import static com.message.chatservice.utils.SerializerUtils.deserializeFromJsonBytes;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final SocketProducer socketProducer;
    private final NotificationService notificationService;
    private static final String GROUP_ACTION_ID = "GROUP_ACTION_ID";
    private static final String FRIEND_NOTIFICATION_ID = "FRIEND_NOTIFICATION_ID";

    @KafkaListener(topics = GROUP_ACTION, groupId = GROUP_ACTION_ID)
    public void groupActionListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(group action) topic : {}, offset: {}, partition: {}, timestamp: {}, data: {}",
                meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), new String(data));
        handleNotification(data, meta, ack);
    }

    @KafkaListener(topics = FRIEND_NOTIFICATION, groupId = FRIEND_NOTIFICATION_ID)
    public void friendRequestListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(group action) topic : {}, offset: {}, partition: {}, timestamp: {}, data: {}",
                meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), new String(data));
        handleNotification(data, meta, ack);
    }

    private void handleNotification(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        try {
            Notification notification = deserializeFromJsonBytes(data, Notification.class);
            notificationService.saveNotification(notification);
            socketProducer.publishNotification(notification.convertToDTO());
            ack.acknowledge();
            log.info("ack events: {}", new String(data));
        } catch (Exception ex) {
            ack.nack(Duration.ofMillis(100).toMillis());
            log.error("(group action) topic: {}, offset: {}, partition: {}, timestamp: {}",
                    meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), ex);
        }
    }
}
