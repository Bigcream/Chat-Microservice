package com.message.chatservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    NOT_FOUND_CONTACT("NOT FOUND CONTACT", HttpStatus.NOT_FOUND),
    NOT_FOUND_CONTACT_PENDING("NOT FOUND CONTACT PENDING", HttpStatus.NOT_FOUND),
    NOT_FOUND_USER("NOT FOUND USER", HttpStatus.NOT_FOUND),
    NOT_FOUND_MESSAGE("NOT FOUND MESSAGE", HttpStatus.NOT_FOUND),
    NOT_FOUND_ROOM_CHAT("NOT FOUND ROOM CHAT", HttpStatus.NOT_FOUND),
    NOT_FOUND_FILE("NOT FOUND FILE", HttpStatus.NOT_FOUND),
    CONTACT_ALREADY_EXIST("CONTACT ALREADY EXIST", HttpStatus.CONFLICT);
    private final String errorName;
    private final HttpStatus status;
}
