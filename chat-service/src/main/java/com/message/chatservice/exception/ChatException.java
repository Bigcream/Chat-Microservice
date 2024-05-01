package com.message.chatservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ChatException extends Exception{
    private final HttpStatus httpStatus;

    public ChatException(ErrorType errorType){
        super(errorType.getErrorName());
        this.httpStatus = errorType.getStatus();
    }
}
