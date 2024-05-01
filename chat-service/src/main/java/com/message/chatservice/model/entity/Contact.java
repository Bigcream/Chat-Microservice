package com.message.chatservice.model.entity;

import com.message.chatservice.model.dto.ContactDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "contact")
@Entity
@ToString
public class Contact {

    @Id
    @GeneratedValue(generator="contact_id_seq")
    @SequenceGenerator(name = "contact_id_seq", sequenceName = "contact_id_seq", allocationSize = 1)
    private Long id;

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String avatar;

    @OneToMany(mappedBy = "contact")
    private List<ContactMember> contactMember;

    @OneToMany(mappedBy = "contact")
    private List<RoomMember> roomMembers;

    @OneToMany(mappedBy = "contact")
    private List<Message> messages;

    public ContactDTO convertDTO(){
        return ContactDTO.builder()
                .id(this.getId())
                .email(this.getEmail())
                .username(this.getUsername())
                .avatar(this.getAvatar())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .userId(this.getUserId())
                .build();
    }
}
