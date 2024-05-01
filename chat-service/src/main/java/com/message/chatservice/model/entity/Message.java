package com.message.chatservice.model.entity;

import com.message.chatservice.model.dto.MessageDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "message")
@Entity
public class Message {

    @Id
    @GeneratedValue(generator="group_id_seq")
    @SequenceGenerator(name = "group_id_seq", sequenceName = "group_id_seq", allocationSize = 1)
    private Long id;

    private String content;

    private LocalDateTime timeCreated;

    private LocalDateTime timeSeen;

    private Long roomChatId;

    private Long contactId;

    private Long receiverId;

    private String state;

    private Boolean deleted;

    private Long replyTo;

    @Transient
    private String type;

    @Transient
    private String avatar;

    @Transient
    private String groupName;

    @Transient
    private List<Long> memberIds;

    @Transient
    private String dateFormat;

    @Transient
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactId", insertable = false, updatable = false)
    private Contact contact;
    public MessageDTO convertDTO(){
        return MessageDTO.builder()
                .id(this.getId())
                .date(this.getDateFormat())
                .state(this.getState())
                .sender(this.getContact().convertDTO())
                .content(this.getContent())
                .replyTo(this.getReplyTo())
                .deleted(this.getDeleted())
                .build();
    }
}
