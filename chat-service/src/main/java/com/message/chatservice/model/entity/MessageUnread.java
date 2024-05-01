package com.message.chatservice.model.entity;

import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "message_unread")
@Entity
public class MessageUnread {

    @Id
    @GeneratedValue(generator="message_unread_id_seq")
    @SequenceGenerator(name = "message_unread_id_seq", sequenceName = "message_unread_id_seq", allocationSize = 1)
    private Long id;

    private Long contactId;

    private Long messageUnread;

    private Long roomChatId;
}
