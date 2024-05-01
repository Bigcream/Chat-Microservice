package com.message.chatservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "room_chat_admin")
@Entity
public class RoomChatAdmin {

    @Id
    @GeneratedValue(generator="room_chat_admin_id_seq")
    @SequenceGenerator(name = "room_chat_admin_id_seq", sequenceName = "room_chat_admin_id_seq", allocationSize = 1)
    private Long id;

    private Long roomChatId;

    private Long adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomChatId", insertable = false, updatable = false)
    @JsonIgnore
    private RoomChat roomChat;
}
