package com.toppica.gateway.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
