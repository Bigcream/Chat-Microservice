package com.message.chatservice.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MessageKafka {

    private String data;

    private String content;

    private LocalDateTime timeCreated;

    private LocalDateTime timeSeen;

    private Long roomChatId;

    private Long contactId;

    private Long receiverId;

    private String state;
}
