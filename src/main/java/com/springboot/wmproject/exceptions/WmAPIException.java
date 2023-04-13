package com.springboot.wmproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class WmAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public WmAPIException(HttpStatus status,String message){
        this.status = status;
        this.message = message;
    }

    public WmAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
