package com.toppica.gateway.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String refresh_token;
    private String email;
    private String firstName;
    private String lastName;
}
