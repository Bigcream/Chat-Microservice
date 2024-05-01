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
@Table(name = "contact_member")
@Entity
public class ContactMember {

    @Id
    @GeneratedValue(generator="contact_member_id_seq")
    @SequenceGenerator(name = "contact_member_id_seq", sequenceName = "contact_member_id_seq", allocationSize = 1)
    private Long id;

    private String currentContact;

    private String addContact;

    private Long receiverId;

    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact contact;
}
