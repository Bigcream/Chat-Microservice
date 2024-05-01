package com.message.chatservice.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContactDTO {

    private String currentEmail;

    private String addEmail;

    private Long id;

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String avatar;

    private boolean isOnline;
}
