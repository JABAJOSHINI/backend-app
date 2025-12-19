package com.marine.customerservice.Exception;

public class UserNotFoundException extends APIException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
