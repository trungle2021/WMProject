package com.springboot.wmproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public UserNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("Username not found with %s: '%s'",fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}

