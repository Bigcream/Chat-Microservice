package com.message.chatservice.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationPayload {

    private String title;

    private String content;

    private String avatar;
}
