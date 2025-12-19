package com.marine.customerservice.Exception;

public class UserAlreadyExistException extends APIException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
