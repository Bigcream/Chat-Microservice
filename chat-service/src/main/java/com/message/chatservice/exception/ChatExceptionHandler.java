package com.message.chatservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ChatExceptionHandler extends IOException {
    private static final String DEFAULT_MESSAGE_ERROR = "server error";

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<String> handlerException(ChatException chatException){
        return new ResponseEntity<>(chatException.getMessage(), chatException.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlerException(){
        return new ResponseEntity<>(DEFAULT_MESSAGE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
