package com.message.chatservice.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ConversationResponse {
    private List<ContactDTO> contacts;
    private String draftMessage;
    private Long id;
    private List<MessageDTO> messages;
    private String type;
    private Long unread;
    private String avatar;
    private String name;
    private List<Long> admins;
}
