package com.toppica.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;

@RestControllerAdvice
public class GatewayExceptionHandler extends IOException {

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<String> handlerException(GatewayException gatewayException){
        return new ResponseEntity<>(gatewayException.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(WebApplicationException.class)
    public ResponseEntity<String> handlerException(WebApplicationException applicationException){
        return new ResponseEntity<>(applicationException.getMessage(), HttpStatus.valueOf(applicationException.getResponse().getStatus()));
    }
}
