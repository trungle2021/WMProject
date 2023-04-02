package com.springboot.wmproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class RefreshTokenNotFoundException extends RuntimeException{

    public RefreshTokenNotFoundException() {
    }
    public RefreshTokenNotFoundException(String message) {

        super(message);
    }

}

