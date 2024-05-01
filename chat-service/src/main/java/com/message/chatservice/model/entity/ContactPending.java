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
@Table(name = "contact_pending")
@Entity
public class ContactPending {

    @Id
    @GeneratedValue(generator="contact_pending_id_seq")
    @SequenceGenerator(name = "contact_pending_id_seq", sequenceName = "contact_pending_id_seq", allocationSize = 1)
    private Long id;

    private String currentContact;

    private String addContact;

    private Long status;

    private Long receiverId;

    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact contact;
}
