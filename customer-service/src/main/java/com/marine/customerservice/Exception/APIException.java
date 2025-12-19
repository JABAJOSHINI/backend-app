package com.marine.customerservice.Exception;

public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}