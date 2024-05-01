package com.message.chatservice.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MessageDTO {
    private String content;
    private String date;
    private Long id;
    private ContactDTO sender;
    private String state;
    private Long roomChatId;
    private String notification;
    private Boolean deleted;
    private Long replyTo;
    private Boolean isNewRoom;
    private List<String> memberEmails;
}
