package com.message.chatservice.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "room_member")
@Entity
public class RoomMember {

    @Id
    @GeneratedValue(generator="room_member_id_seq")
    @SequenceGenerator(name = "room_member_id_seq", sequenceName = "room_member_id_seq", allocationSize = 1)
    private Long id;

    private Long roomChatId;

    private Long contactId;

    private Long status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomChatId", insertable = false, updatable = false)
    private RoomChat roomChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactId", insertable = false, updatable = false)
    private Contact contact;
}
