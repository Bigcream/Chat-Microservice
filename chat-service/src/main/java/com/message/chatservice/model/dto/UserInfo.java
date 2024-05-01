package com.message.chatservice.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfo {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
