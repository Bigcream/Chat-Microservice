package com.message.chatservice.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RoomInfoDTO {
    private Long id;
    private String name;
    private String avatar;
}
