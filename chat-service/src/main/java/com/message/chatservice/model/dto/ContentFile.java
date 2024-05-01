package com.message.chatservice.model.dto;

import lombok.*;
import org.springframework.http.MediaType;

import java.awt.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentFile {

    private Long id;

    private String name;

    private String type;

    private String path;

    private long size;

    private byte[] bytes;

    private MediaType mediaType;

    public MediaType getMediaType(){
        return MediaType.valueOf(this.getType());
    }
}
