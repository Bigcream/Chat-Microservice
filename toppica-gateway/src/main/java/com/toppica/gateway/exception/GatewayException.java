package com.toppica.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GatewayException extends Exception {

    public GatewayException(String message){
        super(message);
    }
}
