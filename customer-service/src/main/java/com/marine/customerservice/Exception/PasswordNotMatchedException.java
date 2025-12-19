package com.marine.customerservice.Exception;

public class PasswordNotMatchedException extends APIException {
    public PasswordNotMatchedException(String message) {
        super(message);
    }
}
