package com.message.chatservice.model.entity;

import com.message.chatservice.model.dto.NotificationDTO;
import com.message.chatservice.model.dto.NotificationPayload;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.message.chatservice.utils.DateTimeUtil.formatTimeNotification;
import static com.message.chatservice.utils.SerializerUtils.deserializeFromJsonString;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "notification")
@Entity
@ToString
public class Notification {

    @Id
    @GeneratedValue(generator="notification_id_seq")
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notification_id_seq", allocationSize = 1)
    private Long id;

    private String payload;

    private String type;

    private Long status;

    private String senderEmail;

    private String receiverEmail;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Transient
    private String topic;

    @Transient
    private String timeFormat;

    public NotificationDTO convertToDTO(){
        return NotificationDTO.builder()
                .id(this.getId())
                .sender(this.getSenderEmail())
                .receiver(this.getReceiverEmail())
                .payload(deserializeFromJsonString(this.getPayload(), NotificationPayload.class))
                .timeFormat(formatTimeNotification(this.getUpdatedDate()))
                .type(this.getType())
                .build();
    }

}
