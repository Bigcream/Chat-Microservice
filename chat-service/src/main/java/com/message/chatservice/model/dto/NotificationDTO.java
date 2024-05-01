package com.message.chatservice.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDTO {

    private Long id;

    private NotificationPayload payload;

    private String sender;

    private String receiver;

    private String type;

    private String timeFormat;
}
