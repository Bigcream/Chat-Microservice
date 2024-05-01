package com.message.chatservice.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RoomChatDTO {
    private Long id;
    private String type;
    private long unread;
    private String name;
    private String avatar;
    private MessageDTO message;
    private String draftMessage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
