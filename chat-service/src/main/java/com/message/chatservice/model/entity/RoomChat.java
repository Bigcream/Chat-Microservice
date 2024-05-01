package com.message.chatservice.model.entity;

import com.message.chatservice.model.dto.RoomChatDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "room_chat")
@Entity
public class RoomChat {

    @Id
    @GeneratedValue(generator="room_chat_id_seq")
    @SequenceGenerator(name = "room_chat_id_seq", sequenceName = "room_chat_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private Long latestMessageId;

    private String avatar;

    private String type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Transient
    private Message message;

    @Transient
    private long messageUnread;

    @OneToMany(mappedBy = "roomChat")
    private List<RoomMember> roomMembers;

    @OneToMany(mappedBy = "roomChat")
    private List<RoomChatAdmin> roomChatAdmins;

    public RoomChatDTO convertDTO(){
        RoomChatDTO roomChatDTO = RoomChatDTO.builder()
                .id(this.getId())
                .unread(this.getMessageUnread())
                .type(this.getType())
                .name(this.getName())
                .avatar(this.getAvatar())
                .draftMessage("")
                .createdDate(this.getCreatedDate())
                .updatedDate(this.getUpdatedDate())
                .build();
        if(message != null){
            roomChatDTO.setMessage(this.message.convertDTO());
        }
        return roomChatDTO;
    }
}
